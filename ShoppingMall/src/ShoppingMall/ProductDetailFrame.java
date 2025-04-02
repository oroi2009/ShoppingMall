package ShoppingMall;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ShoppingMall.MyFrame.BuyAction;

import java.util.*;
import java.util.List;

public class ProductDetailFrame extends JFrame {
	private UserFileManager UFM = new UserFileManager("./UserList.json");
	private ProductFileManager PFM = new ProductFileManager("./product.json");
	private Product product;
	private User user;
	private String prosuctimagename;
	private String productDetail, reviews;
	private int price;
	private double star;
	private ImageIcon productImage;
	private JLabel productImageLabel, productName, producPrice, reviewTitle, rate;
	private JTextArea details, reviewText, review;
	private JButton back, bag, buy, reviewButton;
	private ChangeImage imageChanger;
	private BGMAudio BGMPanel;
	ProductDetailFrame(BGMAudio BGMPanel, Product product, User user) {
		super("J픽셀 스튜디오");
		this.BGMPanel = BGMPanel;
		this.user = user;
		this.product = product;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		add(new DetailPage());
		setSize(415, 750);
		setVisible(true);
	}
	class DetailPage extends JPanel {
		DetailPage() {
			prosuctimagename = product.getImagePath()[0];
			productDetail = product.getDetails();
			reviews = product.getReviews();
			setLayout(null);
			setBackground(new Color(40, 40, 40));
			//mian Frame으로 가는 버튼
			back = new JButton("main");
			back.setSize(80, 30);
			back.setLocation(310, 5);
	        back.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색


			back.addActionListener(new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
		        	 int x = SwingUtilities.getWindowAncestor(DetailPage.this).getX();
		             int y = SwingUtilities.getWindowAncestor(DetailPage.this).getY();
		             
		             // MyFrame 객체 생성 후 위치 설정
		             MyFrame myFrame = new MyFrame(BGMPanel, user);
		             myFrame.setLocation(x, y); // 현재 창 위치로 설정
		             myFrame.setVisible(true);
		             imageChanger.interrupt();
		             // 현재 LoginFrame 닫기
		             dispose();
		           }
		    }); 
			add(back);
			BGMPanel.setLocation(5, 0);
	        add(BGMPanel);
			//상품 사진
			productImage = new ImageIcon(prosuctimagename);
			Image scaledImage = productImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
			productImageLabel = new JLabel(scaledIcon);
			productImageLabel.setSize(380, 300);
			productImageLabel.setLocation(10, 40);
			productImageLabel.setBackground(Color.WHITE);
			productImageLabel.setOpaque(true);
			add(productImageLabel);
            imageChanger = new ChangeImage(productImageLabel);
            imageChanger.start(); // 스레드 실행
			//상품 이름
			productName = new JLabel(product.getName());
			productName.setSize(380, 30);
			productName.setLocation(10, 340);
			productName.setFont(new Font("Serif", Font.PLAIN, 17));
			productName.setForeground(Color.WHITE);
			add(productName);
			//상품 금액
			price = product.getPrice();
			producPrice = new JLabel(String.valueOf(price) + "원");
			producPrice.setSize(200, 20);
			producPrice.setLocation(10, 370);
			producPrice.setFont(new Font("Serif", Font.PLAIN, 17));
			producPrice.setForeground(Color.WHITE);
			add(producPrice);
			//장바구니 넣기
			bag = new JButton("장바구니");
			bag.setSize(90, 30);
			bag.setLocation(235, 370);
	        bag.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색

			bag.addActionListener(new ActionListener() {
    	        @Override // 장바구니 추가
    			public void actionPerformed(ActionEvent e) {
    	        	String addProdcut = product.getName();
    	        	Vector<String> cart = user.getcart();
    	        	for (String name : cart) { 
    	        		if (name.equals(addProdcut)) { 
    	        			System.out.println("이미 장바구니에 있습니다.");
    	        			return; 
    	        		}
    	        	}
    	        	user.addcart(addProdcut);
    	        	UFM.writeToFile(user);
    	        }
    		});
			add(bag);
			//구매 버튼
			buy = new JButton("구매");
			buy.setSize(60, 30);
			buy.setLocation(330, 370);
	        buy.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색
			buy.addActionListener(new BuyAction(product.getPrice()));
			if (user != null && user.getMoney() < product.getPrice())
				buy.setForeground(Color.RED);
			add(buy);
			//상품 상세 설명
			details = new JTextArea(productDetail, 6, 30);
			details.setEditable(false); //내용 수정 못하게
			details.setFocusable(false); // 마우스 포인터로 포커스 받지 않게 함
			JScrollPane scrollPane = new JScrollPane(details);
			scrollPane.setBounds(10, 400, 380, 150); //10,400에서 크기가 380x150
			add(scrollPane);
			//리뷰 달기
			reviewTitle = new JLabel("별점, 리뷰 작성 : ");
			reviewTitle.setSize(110, 30);
			reviewTitle.setLocation(10, 555);
			reviewTitle.setFont(reviewTitle.getFont().deriveFont(13f)); //글꼴 다 그대로 두고 글자 크기만 바꾸는 방법 
			reviewTitle.setForeground(Color.WHITE);
			add(reviewTitle);
			//리뷰 텍스트영역
			reviewText = new JTextArea("5, 디자인이 이뻐요!!", 2, 10);
			scrollPane = new JScrollPane(reviewText);
			scrollPane.setBounds(120, 555, 200, 30); // 위치와 크기 설정
			add(scrollPane);
			//리뷰 확인 버튼
			reviewButton = new JButton("작성");
			reviewButton.setSize(60, 30);
			reviewButton.setLocation(325,555);
	        reviewButton.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색
			reviewButton.addActionListener(new ReviewRate());
			if (user != null && !product.checkBuy(user)) {
        		reviewButton.setForeground(Color.RED);
			}
			add(reviewButton);
			//별점
			star = product.getRate();
			rate = new JLabel("별점 : " + String.valueOf(star) + " / 5.0          리뷰 : " + product.getReviewNum() + "개");
			rate.setSize(380, 20);
			rate.setLocation(10, 590); //heigth 610
			rate.setFont(rate.getFont().deriveFont(15f)); //글꼴 다 그대로 두고 글자 크기만 바꾸는 방법 
			rate.setForeground(Color.WHITE);
			add(rate);
			//리뷰
			review = new JTextArea(product.getReviews(), 6, 30);
			if (product.getReviews() == "")
				review.setText("리뷰가 없습니다.");
			review.setEditable(false); //내용 수정 못하게
			review.setFocusable(false); // 마우스 포인터로 포커스 받지 않게 함
			scrollPane = new JScrollPane(review);
			scrollPane.setBounds(10, 610, 380, 100); //10,610에서 크기가 380x100
			add(scrollPane);
		}
	}
	class BuyAction implements ActionListener {
		private int price;
		BuyAction(int price) { this.price = price; }
		@Override
		public void actionPerformed(ActionEvent e) { 
			int userMoney = user.getMoney();
			JButton buy = (JButton) e.getSource();
			if (userMoney < price) {
				System.out.println("돈이 부족합니다. 현재 보유한 금액: " + userMoney);
				return;
			}
			user.changeMoney(price, "-");
        	if (!product.checkBuy(user)) user.addbuyProduct(product.getName());
			user.removecart(product.getName());
			UFM.writeToFile(user);
			if (user.getMoney() < product.getPrice())
				buy.setForeground(Color.RED);
			else
				buy.setForeground(Color.BLACK);
			if (product.checkBuy(user))
        		reviewButton.setForeground(Color.BLACK);
			repaint();
		}
	}
	class ReviewRate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
        	if (!product.checkBuy(user)) { System.out.println("구매하지 않은 제품 입니다."); return; }
        	if (product.checkReviewer(user)) { System.out.println("이미 리뷰를 다셨습니다."); return; }
        	String reviewAndRate = reviewText.getText();
            if (reviewAndRate == null || reviewAndRate.trim().isEmpty()) { return; }
            String[] parts = reviewAndRate.split(",");
            if (parts.length < 2) { return; }
        	double newRate;
        	try {
        		newRate = Double.parseDouble(parts[0]); 
            } catch (NumberFormatException ex) { return; }
        	newRate = Math.round(newRate * 10.0) / 10.0;
        	if (newRate > 5) newRate = 5;
        	if (newRate < 0) newRate = 0;
    		String newReview = user.getId() + "(" + newRate + "점):" + parts[1];
        	for (int i = 2; i < parts.length; ++i)
        		newReview += "," + parts[i];
        	product.setRate(newRate);
        	product.addReview(newReview);
        	product.addReviewerId(user.getId());
        	PFM.writeToFile(product);
        	review.setText(product.getReviews());
        	rate.setText("별점 : " + product.getRate() + " / 5.0          리뷰 : " + product.getReviewNum() + "개");
			repaint();
		}
	}
	class ChangeImage extends Thread {
		private JComponent target;
		private ImageIcon[] images = new ImageIcon[2];
		private int currentIndex; // 현재 이미지를 추적하는 인덱스
		public ChangeImage(JComponent target) {
			currentIndex = 0;
			this.target = target;
			ImageIcon[] imagePaths = new ImageIcon[2];
			imagePaths[0] = new ImageIcon(product.getImagePath()[0]);
			imagePaths[1] = new ImageIcon(product.getImagePath()[1]);
			Image scaledImage1 = imagePaths[0].getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
			Image scaledImage2 = imagePaths[1].getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
			images[0] = new ImageIcon(scaledImage1);
			images[1] = new ImageIcon(scaledImage2);
		}
		@Override
		public void run() {
		    while (true) {
		        try {
                    JLabel label = (JLabel) target;
                    label.setIcon(images[currentIndex]);
                    label.repaint();
		            // 인덱스 업데이트 (배열 순환)
		            currentIndex = (currentIndex + 1) % images.length;
		            // 2초 대기
		            Thread.sleep(2000);
		        } catch (InterruptedException e) { }
		    }
		}
 }
}