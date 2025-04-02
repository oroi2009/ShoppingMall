package ShoppingMall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;

public abstract class FileManager {
    protected String filePath;

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    // 공통 JSON 파일 읽기
    protected JSONArray readFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONArray(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    // 공통 JSON 파일 쓰기
    protected void writeFile(JSONArray jsonArray) {
        try {
            // 들여쓰기 4칸으로 JSON 데이터 저장
            Files.write(Paths.get(filePath), jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 추상 메서드
    public abstract Object readFromFile(String key, int start, int limit);
    public abstract void writeToFile(Object data);
}

// 사용자 파일 관리 클래스
class UserFileManager extends FileManager {

    public UserFileManager(String filePath) {
        super(filePath);
    }

    @Override
    public User readFromFile(String key, int start, int limit) {
        JSONArray jsonArray = readFile();

        // 특정 ID와 일치하는 사용자 찾기
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (!obj.has("id")) continue;
            if (obj.getString("id").equals(key)) {
                // buyProduct 필드를 Vector<String>으로 변환
                Vector<String> buyProduct = new Vector<>();
                JSONArray buyProductArray = obj.getJSONArray("buyProduct");
                for (int j = 0; j < buyProductArray.length(); j++) {
                    buyProduct.add(buyProductArray.getString(j));
                }
                Vector<String> cart = new Vector<>();
                JSONArray cartArray = obj.getJSONArray("cart");
                for (int j = 0; j < cartArray.length(); j++) {
               	 cart.add(cartArray.getString(j));
                }

                // User 객체 생성 및 반환
                return new User(
                    obj.getString("id"),
                    obj.getString("password"),
                    obj.getString("tel"),
                    obj.getString("address"),
                    obj.getInt("money"),
                    obj.getBoolean("loggedin"),
                    buyProduct,
                    cart
                );
            }
        }
        return null; // 일치하는 사용자 없음
    }

    @Override
    public void writeToFile(Object data) {
        JSONArray jsonArray = readFile();
        User user = (User) data;

        // 사용자 ID로 기존 데이터 탐색
        boolean userExists = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString("id").equals(user.getId())) {
                // 기존 사용자 정보 업데이트
                obj.put("password", user.getPassword());
                obj.put("tel", user.getTel());
                obj.put("address", user.getAddress());
                obj.put("money", user.getMoney());
                obj.put("loggedin", user.isLoggedIn());

                // buyProduct 업데이트
                JSONArray buyProductArray = new JSONArray(user.getbuyProduct());
                obj.put("buyProduct", buyProductArray);

                // cart 업데이트
                JSONArray cartArray = new JSONArray(user.getcart());
                obj.put("cart", cartArray);

                userExists = true;
                break;
            }
        }

        // 기존 사용자가 없으면 새로 추가
        if (!userExists) {
            JSONObject newUser = new JSONObject();
            newUser.put("id", user.getId());
            newUser.put("password", user.getPassword());
            newUser.put("tel", user.getTel());
            newUser.put("address", user.getAddress());
            newUser.put("money", user.getMoney());
            newUser.put("loggedin", user.isLoggedIn());

            // buyProduct 추가
            JSONArray buyProductArray = new JSONArray(user.getbuyProduct());
            newUser.put("buyProduct", buyProductArray);

            // cart 추가
            JSONArray cartArray = new JSONArray(user.getcart());
            newUser.put("cart", cartArray);

            jsonArray.put(newUser);
        }

        // 변경된 JSON 데이터를 파일에 저장
        writeFile(jsonArray);
    }

}

// 상품 파일 관리 클래스
class ProductFileManager extends FileManager {

    public ProductFileManager(String filePath) {
        super(filePath);
    }

    @Override
    public List<Product> readFromFile(String key, int start, int limit) {
        List<Product> products = new ArrayList<>();
        JSONArray jsonArray = readFile();

        // 키워드 기반 필터링
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            // imagePath 필드를 JSONArray로 처리
            JSONArray imagePathArray = obj.getJSONArray("imagePath");
            String[] imagePaths = new String[imagePathArray.length()];
            for (int j = 0; j < imagePathArray.length(); j++) {
                imagePaths[j] = imagePathArray.getString(j);
            }

            // keyword 필드를 JSONArray로 처리
            boolean keywordMatch = false;
            if (obj.has("keyword")) { // keyword 필드가 있는 경우만 처리
                JSONArray keywordArray = obj.getJSONArray("keyword");
                for (int j = 0; j < keywordArray.length(); j++) {
                    if (keywordArray.getString(j).equalsIgnoreCase(key)) {
                        keywordMatch = true;
                        break;
                    }
                }
            }
            Vector<String> reviewerId = new Vector<>();
            JSONArray reviewerIdArray = obj.getJSONArray("reviewerId");
            for (int j = 0; j < reviewerIdArray.length(); j++) {
            	reviewerId.add(reviewerIdArray.getString(j));
            }

            // 키워드(상품명, 카테고리, keyword 배열)로 필터링
            if (obj.getString("name").equalsIgnoreCase(key) || 
                obj.getString("category").equalsIgnoreCase(key) || 
                keywordMatch) {

                // Product 객체 생성
                products.add(new Product(
                    obj.getString("name"),
                    obj.getString("category"),
                    imagePaths, // imagePath 배열
                    obj.getInt("price"),
                    obj.getString("details"),
                    obj.getString("reviews"),
                    obj.getInt("reviewNum"),
                    obj.getDouble("rate"),
                    reviewerId
                ));
            }
        }

        // 페이징 처리
        int end = Math.min(start + limit, products.size());
        return products.subList(start, end);
    }

    @Override
    public void writeToFile(Object data) {
        JSONArray jsonArray = readFile();
        Product product = (Product) data;

        // 상품 이름으로 기존 데이터 탐색
        boolean productExists = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString("name").equals(product.getName())) {
                // 기존 상품 정보 업데이트
                obj.put("category", product.getCategory());

                // imagePath 업데이트
                JSONArray imagePathArray = new JSONArray();
                for (String path : product.getImagePath()) {
                    imagePathArray.put(path);
                }
                obj.put("imagePath", imagePathArray);

                obj.put("price", product.getPrice());
                obj.put("details", product.getDetails());
                obj.put("reviews", product.getReviews());
                obj.put("reviewNum", product.getReviewNum());
                obj.put("rate", product.getRate());
                
                // reviewerId 업데이트
                JSONArray reviewerIdArray = new JSONArray(product.getReviewerId());
                obj.put("reviewerId", reviewerIdArray);

                productExists = true;
                break;
            }
        }

        // 기존 상품이 없으면 새로 추가
        if (!productExists) {
            JSONObject newProduct = new JSONObject();
            newProduct.put("name", product.getName());
            newProduct.put("category", product.getCategory());

            // imagePath 추가
            JSONArray imagePathArray = new JSONArray();
            for (String path : product.getImagePath()) {
                imagePathArray.put(path);
            }
            newProduct.put("imagePath", imagePathArray);

            newProduct.put("price", product.getPrice());
            newProduct.put("details", product.getDetails());
            newProduct.put("reviews", product.getReviews());
            newProduct.put("reviewNum", product.getReviewNum());
            newProduct.put("rate", product.getRate());
            
            // reviewerId 업데이트
            JSONArray reviewerIdArray = new JSONArray(product.getReviewerId());
            newProduct.put("reviewerId", reviewerIdArray);

            jsonArray.put(newProduct);
        }

        // 변경된 JSON 데이터를 파일에 저장
        writeFile(jsonArray);
    }
}