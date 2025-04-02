package ShoppingMall;
import java.util.*;

public class User {
	private String id;
	private String ps;
    private String phoneNumber;
    private String address;
    private int money;
    private boolean loggedin;
    private Vector <String> buyProduct;
    private Vector <String> cart;
    
    public User(String id, String ps, String phoneNumber, String address,int money,boolean loggedin, Vector<String> buyProduct,Vector<String> cart) {
    	this.id = id;
    	this.ps = ps;
    	this.phoneNumber = phoneNumber;
    	this.address = address;
    	this.money = money;
    	this.loggedin = loggedin;
        this.buyProduct = buyProduct != null ? buyProduct : new Vector<>(); // 초기화
        this.cart = cart != null ? cart : new Vector<>(); // 초기화

    }
    //ID 관련
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    //PS 관련
    public String getPassword() { return ps; }
    public void setPassword(String ps) { this.ps = ps; }
    //Tel 관련
    public String getTel() { return phoneNumber; }
    public void setTel(String phoneNumber) { this.phoneNumber = phoneNumber; }
    //Address 관련
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    //Money 관련
    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }
    //buyProduct 관련
    public Vector<String> getbuyProduct() { return buyProduct; }
    public void addbuyProduct(String product) { this.buyProduct.add(product); }
    
    public Vector<String> getcart() { return cart; }
    public void addcart(String cart_product) { this.cart.add(cart_product); }
    public void removecart(String cart_product) { this.cart.remove(cart_product); }
    public void resetcart() { this.cart.clear(); }
    
    public void changeMoney(int amount, String operation) {
        if (operation.equals("+")) {
            this.money += amount;
        } else if (operation.equals("-")) {
            this.money -= amount;
        } else {
            throw new IllegalArgumentException("Invalid operation. Use + or -.");
        }
    }
    
    public boolean isLoggedIn() {return loggedin;}

}