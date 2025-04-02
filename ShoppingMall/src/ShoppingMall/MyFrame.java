package ShoppingMall;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.List;


class FirstPanel extends JPanel{
	private MyFrame mainFrame;
	private User user;
	private BGMAudio BGMPanel;
	LoginFrame lf;
	UserInfoFrame uif;
	public String loggedInUser;
	private JLabel titleLabel;
    JButton lg_btn = new JButton("로그인");
    
	public FirstPanel(BGMAudio Aduio, MyFrame mainF, User user) {
		this.user = user;
		mainFrame = mainF;
		lf = new LoginFrame();
        setBackground(new Color(40, 40, 40)); // 모던한 파스텔 블랙 (차콜 그레이)
        setLayout(new BorderLayout());
        
        BGMPanel = Aduio;
        add(BGMPanel, BorderLayout.WEST);
        
        titleLabel = new JLabel("JPixel Universe");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 중앙 정렬
        titleLabel.setFont(titleLabel.getFont().deriveFont(25f));
        titleLabel.addMouseListener(new MyMouseEvent());
        add(titleLabel, BorderLayout.CENTER); // 중앙에 라벨 추가
        
        // "장바구니" 버튼과 "로그인" 버튼은 오른쪽에 배치
        lg_btn = new JButton("로그인");
        lg_btn.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색

        JButton cart_btn = new JButton("장바구니");
        cart_btn.setBackground(Color.WHITE); // 장바구니 버튼 배경색 흰색
        

        JPanel rightPanel = new JPanel();
        
        cart_btn.addActionListener(new ActionListener() { // 장바구니 버튼 누르면 과련 항목들 표시
	        @Override
			public void actionPerformed(ActionEvent e) {
	        	Vector<String> cartItems = user.getcart();
        		ProductFileManager PFM = mainFrame.getFileManager();
        		List<Product> items = new ArrayList<Product>();
	        	for (String name : cartItems) {
	        		List<Product> searchProduct = PFM.readFromFile(name, 0, 1);
	        		items.addAll(searchProduct);
	        	}
	        	mainFrame.changeProductPanel(items);
	        }
		});
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 오른쪽 정렬
        rightPanel.setBackground(new Color(40, 40, 40));
        rightPanel.add(cart_btn);
        rightPanel.add(lg_btn);
        // 오른쪽 패널을 추가
        add(rightPanel, BorderLayout.EAST);
        
        lg_btn.addActionListener(new ActionListener() {
	        @Override
			public void actionPerformed(ActionEvent e) {
				// FirstPanel의 위치를 얻어 로그인 창에 전달 현재 패널안에 있기 때문에
                if(user == null) {
	        	int x = SwingUtilities.getWindowAncestor(FirstPanel.this).getX();
                int y = SwingUtilities.getWindowAncestor(FirstPanel.this).getY();

                // 로그인 창을 해당 위치로 띄우기
                lf.setLocation(x, y);
				lf.setVisible(true);
				BGMPanel.close();
				//gpt 사용
		        SwingUtilities.getWindowAncestor(FirstPanel.this).dispose(); // 부모 창 닫기
                }
                else {
                	int x = SwingUtilities.getWindowAncestor(FirstPanel.this).getX();
                    int y = SwingUtilities.getWindowAncestor(FirstPanel.this).getY();

                    // 로그인 창을 해당 위치로 띄우기
                    uif.setLocation(x, y);
                    uif.setVisible(true);
                    SwingUtilities.getWindowAncestor(FirstPanel.this).dispose();
                }
	        
	        }
		});
	}
	class MyMouseEvent extends MouseAdapter {
		@Override
    	public void mouseClicked(MouseEvent evt) { mainFrame.changeProductPanel(); }
        public void mouseEntered(java.awt.event.MouseEvent evt) {
        	JLabel  title= (JLabel)evt.getSource();
        	title.setForeground(new Color(135, 206, 250));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) { titleLabel.setForeground(Color.WHITE); }
	}
	public void onLoginSuccess(User user) {
	 	String userID = user.getId();
        this.loggedInUser = userID; // 로그인된 사용자 ID 저장
        System.out.println("onLoginSuccess called. UserID: " + userID); // 디버깅 로그
        System.out.println("LoggedInUser updated: " + this.loggedInUser);
        lg_btn.setText("회원 정보"); // 버튼 텍스트 변경
        uif = new UserInfoFrame(BGMPanel, this, loggedInUser);
	}
}

class SearchPanel extends JPanel{
	private MyFrame mainFrame;
	private JTextField searchField;
	public SearchPanel(MyFrame mainF) {
		mainFrame = mainF;
		setLayout(new FlowLayout());
        setBackground(new Color(40, 40, 40)); // 모던한 파스텔 블랙 (차콜 그레이)
		searchField = new JTextField(50);
        // 텍스트 필드 테두리를 진한 파란색으로 설정
        searchField.setBorder(new LineBorder(new Color(100, 149, 237), 2));
        searchField.addActionListener(new MyActionListener());
        
        // 검색 버튼 추가
        add(searchField);
        JButton check = new JButton("검색");
        check.setBackground(Color.WHITE); // 장바구니 버튼 배경색 흰색
        check.addActionListener(new MyActionListener());
		add(check);
	}
	class MyActionListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent evt) {
			mainFrame.changeProductPanel(searchField.getText());
		}
	}
}

class CategoryPanel extends JPanel{
	private MyFrame mainFrame;
	public CategoryPanel(MyFrame mainF) {
		mainFrame = mainF;
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 좌측 정렬 및 아이템 간격 조정

        // 카테고리 항목들
        String[] categories = {"상의", "바지", "아우터", "기타"};
        
        // 카테고리 구역 전체 크기 조정 (500x600 크기에 맞도록)
        setPreferredSize(new Dimension(500, 50)); // 전체 카테고리 패널의 크기 설정

        //for문 수정하기 너무 어색함.
        // 각 카테고리별로 구역 만들기
        for (String category : categories) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.setPreferredSize(new Dimension(145, 40)); // 각 카테고리 구역 크기 설정
            categoryPanel.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
            categoryPanel.setBorder(BorderFactory.createLineBorder(new Color(160, 160, 160))); // 구역 테두리 설정
            categoryPanel.setLayout(new BorderLayout());
            
            // 텍스트를 중앙 정렬
            JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
            categoryLabel.setForeground(Color.WHITE); // 텍스트 색상 흰색

            categoryPanel.add(categoryLabel, BorderLayout.CENTER);
            
            // 호버 효과 추가
            categoryPanel.addMouseListener(new MouseAdapter() {
		        @Override
            	public void mouseClicked(MouseEvent evt) {
            		JPanel clickedPanel = (JPanel) evt.getSource();
            	    JLabel label = (JLabel)clickedPanel.getComponent(0);
            		mainFrame.changeProductPanel(label.getText());
            	}
                public void mouseEntered(MouseEvent evt) {
                    categoryPanel.setBackground(new Color(135, 206, 250)); // 마우스 올리면 색상 변경 (light sky blue)
                }
                public void mouseExited(MouseEvent evt) {
                    categoryPanel.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
                }
            });

            // 구역 추가
            add(categoryPanel);
        }
	}
}

class ProductPanel extends JPanel{
	 private UserFileManager UFM = new UserFileManager("./UserList.json");
	 private List<Product> products;
	 private ArrayList<ImageIcon> images;
	 private ProductDetailFrame DetailFrame;
	 private ChangeImage imageChanger;
	 private User user;
	 private MyFrame mainFrame;
	 public ProductPanel(MyFrame mainFrame, List<Product> p, User user, boolean isCart) {
		    this.mainFrame = mainFrame;
		 	this.user = user;
	        setLayout(new FlowLayout());
	        products = p;
	        images = new ArrayList<>();
	        setLayout(new GridLayout(0, 3));
	      
	        for (int i = 0; i < products.size(); i++) {
	            JPanel itemPanel = new JPanel(); // 이미지와 텍스트를 담을 패널
	            itemPanel.setLayout(new BorderLayout()); // 이미지 위에 텍스트 배치
	            images.add(new ImageIcon(products.get(i).getImagePath()[0]));
	            Image scaledImage = images.get(i).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
	            ImageIcon scaledIcon = new ImageIcon(scaledImage);
	            
	            JLabel imageLabel = new JLabel(scaledIcon); // 이미지 표시
	            JLabel textLabel = new JLabel(products.get(i).getName()); // 텍스트 설명
	            int price = products.get(i).getPrice();
	            JLabel priceLabel = new JLabel(Integer.toString(price) + "원"); // 텍스트 설명
	    		textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    		priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	            Product currentProduct = products.get(i);
	    		JPanel PanelDetail = new JPanel();
	    		PanelDetail.setLayout(new BoxLayout(PanelDetail, BoxLayout.Y_AXIS));
	    		PanelDetail.add(textLabel);
	    		PanelDetail.add(priceLabel);
	    		PanelDetail.setBackground(Color.WHITE);
	            itemPanel.setBackground(Color.WHITE);
	            // 장바구니일 경우
	            if (isCart) {
	            	JButton cancel = new JButton("Cancel");
	            	cancel.setBackground(Color.RED);
	            	cancel.setOpaque(true);
	            	cancel.addActionListener(new ActionListener() {
	        	        @Override // 장바구니 삭제
	        			public void actionPerformed(ActionEvent e) {
	        	        	String cancleProdcut = currentProduct.getName();
	        	        	user.removecart(cancleProdcut);
	        	        	UFM.writeToFile(user);
	        	        	Vector<String> cartItems = user.getcart();
	                		ProductFileManager PFM = mainFrame.getFileManager();
	                		List<Product> items = new ArrayList<Product>();
	        	        	for (String name : cartItems) {
	        	        		List<Product> searchProduct = PFM.readFromFile(name, 0, 1);
	        	        		items.addAll(searchProduct);
	        	        	}
	        	        	mainFrame.changeProductPanel(items);
	        	        	
	        	        	
	        	        }
	        		});
	            	itemPanel.add(cancel, BorderLayout.NORTH);
	            }
	            itemPanel.add(imageLabel, BorderLayout.CENTER);
	            itemPanel.add(PanelDetail, BorderLayout.SOUTH); // 텍스트를 이미지 아래에 배치
	            imageChanger = new ChangeImage(imageLabel, products.get(i));
	            imageChanger.start(); // 스레드 실행
	            		
	            itemPanel.addMouseListener(new MouseAdapter() {
			        @Override
	            	public void mouseClicked(MouseEvent evt) {
	    				// FirstPanel의 위치를 얻어 로그인 창에 전달 현재 패널안에 있기 때문에
	                    int x = SwingUtilities.getWindowAncestor(ProductPanel.this).getX();
	                    int y = SwingUtilities.getWindowAncestor(ProductPanel.this).getY();
	                    DetailFrame = new ProductDetailFrame(mainFrame.getAudio(), currentProduct, user);
	                    // 로그인 창을 해당 위치로 띄우기
	                    DetailFrame.setLocation(x, y);
	                    DetailFrame.setVisible(true);
	    				//gpt 사용
	    		        SwingUtilities.getWindowAncestor(ProductPanel.this).dispose(); // 부모 창 닫기
	            	}
	                public void mouseEntered(java.awt.event.MouseEvent evt) {
	                	PanelDetail.setBackground(new Color(240, 240, 240));
	                    itemPanel.setBackground(new Color(240, 240, 240)); // 호버 시 배경 색상
	                }
	                public void mouseExited(java.awt.event.MouseEvent evt) {
	                	PanelDetail.setBackground(Color.WHITE);
	                    itemPanel.setBackground(Color.WHITE); // 기본 배경 색상
	                }
	            });
	            this.add(itemPanel); // 전체 패널에 추가
	        }
	 }
	 class ChangeImage extends Thread {
			private JComponent target;
			private ImageIcon[] images = new ImageIcon[2];
			private int currentIndex; // 현재 이미지를 추적하는 인덱스
			public ChangeImage(JComponent target, Product product) {
				currentIndex = 0;
				this.target = target;
				ImageIcon[] imagePaths = new ImageIcon[2];
				imagePaths[0] = new ImageIcon(product.getImagePath()[0]);
				imagePaths[1] = new ImageIcon(product.getImagePath()[1]);
				Image scaledImage1 = imagePaths[0].getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				Image scaledImage2 = imagePaths[1].getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				images[0] = new ImageIcon(scaledImage1);
				images[1] = new ImageIcon(scaledImage2);
			}
			@Override
			public void run() {
			    while (true) {
			        try {
	                    JLabel label = (JLabel) target;
	                    label.setIcon(images[currentIndex]);
	                    label.getParent().repaint();
			            // 인덱스 업데이트 (배열 순환)
			            currentIndex = (currentIndex + 1) % images.length;
			            // 2초 대기
			            Thread.sleep(2000);
			        } catch (InterruptedException e) { }
			    }
			}
	 }
	 public void InterruptThread() { imageChanger.interrupt(); }
}
class ColorPanel extends JPanel{
    private JPanel bottomPanel; // 추천 패널이 추가될 위치

	public ColorPanel() {
		setLayout(new BorderLayout());

        // 상단 라벨
        JLabel titleLabel = new JLabel("<추천 컬러>", SwingConstants.LEFT);
   
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10)); // 상하좌우 여백
        add(titleLabel, BorderLayout.NORTH);

        // 바둑판 생성
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 6, 5, 5)); // 2행 8열, 간격 5픽셀
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // 16가지 색상
        Color[] colors = {
	        		new Color(255, 182, 193), // 파스텔 핑크 (Light Pink)
	        	    new Color(255, 0, 0),      // 파스텔 레드 (Red)
	        	    new Color(255, 140, 0), // 진한 주황색 (Dark Orange)
	        	    new Color(255, 228, 181), // 파스텔 베이지 (Moccasin)
	        	    new Color(255, 223, 0), // 조금 더 진한 노란색 (Deep Yellow)
	        	    new Color(34, 139, 34), // 딥한 초록색 (Forest Green)
	        	    new Color(173, 216, 230), // 연하면서 파스텔 느낌의 하늘색 (Pale Sky Blue)
	        	    new Color(0, 0, 128),     // 네이비 (Navy Blue)
	        	    new Color(186, 125, 215), // 밝고 진한 보라색 (Bright Dark Lavender)
	        	    new Color(101, 67, 33),   // 갈색 (Brown)
	        	    new Color(169, 169, 169), // 파스텔 회색 (Light Gray)
	        	    new Color(0, 0, 0), // 파스텔 회색 (Light Gray)
        	    };

        for (Color color : colors) {
            JPanel colorBox = new JPanel();
            colorBox.setBackground(color);
            colorBox.setPreferredSize(new Dimension(50, 25));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //g선생
            colorBox.addMouseListener(new java.awt.event.MouseAdapter() {
            	public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showRecommendationPanel(color);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    colorBox.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // 호버 효과
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 기본 테두리
                }
            });
            
            gridPanel.add(colorBox);
        }
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        showRecommendationPanel(colors[0]);
        add(bottomPanel, BorderLayout.SOUTH);
        add(gridPanel, BorderLayout.CENTER);
    
	}
	private void showRecommendationPanel(Color baseColor) {
        // 기존 추천 패널 제거
        bottomPanel.removeAll();
        
        Color[] recommendedColors;
        String[] colorNames;
        String description;
        // 추천 색상과 설명을 설정
        if (baseColor.equals(new Color(255, 182, 193))) { // light pink
            recommendedColors = new Color[] { new Color(173, 216, 230), new Color(186, 125, 215), new Color(101, 67, 33), new Color(0, 0, 0) };
            colorNames = new String[] { "light blue", "purple", "brown", "black" };
            description = "Pink - 분홍색은 부드럽고 따뜻한 느낌을 주는 색상으로 강한 대비 보다는 부드럽거나 중립적인 색상이 잘 어울립니다.";
        } else if (baseColor.equals(new Color(255, 0, 0))) { // red
            recommendedColors = new Color[] { new Color(64, 164, 223), new Color(255, 253, 208), new Color(0, 0, 255), new Color(255, 223, 0) };
            colorNames = new String[] { "light blue", "beige", "blue", "yellow" };
            description = "Red - 빨간색은 에너지와 활기찬 느낌을 주는 색상으로 강한 대비는 물론 시원한 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(255, 140, 0))) { // orange
            recommendedColors = new Color[] { new Color(0, 0, 128), new Color(255, 253, 208), new Color(70, 130, 180), new Color(255, 253, 208) };
            colorNames = new String[] { "navy", "beige", "steel blue", "beige" };
            description = "Orange - 주황색은 생동감과 따뜻한 느낌을 주는 색상으로 차분한 느낌은 물론 차가운 느낌의 색상과도 잘 어울립니다.";
        } else if (baseColor.equals(new Color(255, 228, 181))) { // 베이지
            recommendedColors = new Color[] { new Color(144, 238, 144), new Color(205, 153, 164), new Color(70, 130, 180), new Color(101, 67, 33) };
            colorNames = new String[] { "light green", "Tuscan", "steel blue", "brown" };
            description = "Beige - 베이지색은 모던하고 부드러운 느낌을 주는 색상으로 따뜻하거나 차가운 색상 모두 잘 어울립니다.";
        } else if (baseColor.equals(new Color(255, 223, 0))) { // yellow
            recommendedColors = new Color[] { new Color(0, 51, 102), new Color(128, 128, 0), new Color(0, 128, 128), new Color(119, 136, 153) };
            colorNames = new String[] { "dark blue", "olive", "turquoise", "gray" };
            description = "Yellow - 노란색은 따뜻하고 밝은 느낌을 주는 색상으로 같은 따뜻한 계열 색상과 가장 잘 어울립니다.";
        } else if (baseColor.equals(new Color(34, 139, 34))) { // green
            recommendedColors = new Color[] { new Color(139, 0, 0), new Color(107, 142, 35), new Color(245, 245, 220), new Color(211, 211, 211) };
            colorNames = new String[] { "dark red", "olive", "beige", "light gray" };
            description = "Green - 초록색은 자연과 같은 느낌으로 안정적이고 산뜻한 느낌을 주는 색상으로 모던하고 따뜻한 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(173, 216, 230))) { // mint
            recommendedColors = new Color[] { new Color(255, 255, 255), new Color(0, 0, 128), new Color(255, 218, 185), new Color(230, 230, 250) };
            colorNames = new String[] { "white", "navy", "peach", "lavender" };
            description = "Mint - 민트색은 차분하고 클래식한 느낌을 주는 색상으로 부드럽고 깔끔한 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(0, 0, 128))) { // navy
            recommendedColors = new Color[] { new Color(34, 139, 34), new Color(169, 169, 169), new Color(0, 0, 128), new Color(72, 0, 90) };
            colorNames = new String[] { "green", "gray", "navy", "purple" };
            description = "Navy - 네이비색은 깊고 세련된 느낌을 주는 색상으로 비슷한 색상 혹은 대비되는 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(186, 125, 215))) { // purple
            recommendedColors = new Color[] { new Color(255, 228, 181), new Color(169, 169, 169), new Color(255, 182, 193), new Color(230, 230, 250) };
            colorNames = new String[] { "beige", "gray", "light pink", "lavender" };
            description = "Purple - 보라색은 부드럽고 우아한 느낌을 주는 색상으로 따뜻하고 모던한 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(101, 67, 33))) { // brown
            recommendedColors = new Color[] { new Color(128, 128, 0), new Color(101, 67, 33), new Color(34, 139, 34), new Color(255, 228, 181) };
            colorNames = new String[] { "olive", "brown", "green", "beige" };
            description = "Brown - 갈색은 고전적이고 세련된 느낌을 주는 색상으로 같은 계열의 색상과 잘 어울립니다.";
        } else if (baseColor.equals(new Color(169, 169, 169))) { // gray
            recommendedColors = new Color[] { new Color(0, 0, 128), new Color(101, 67, 33), new Color(186, 125, 215), new Color(255, 182, 193) };
            colorNames = new String[] { "navy", "brown", "purple", "light pink" };
            description = "Gray - 회색은 중립적이고 고급스러운 느낌을 주는 색상으로 어두운 계열 혹은 은은한 색을 추천드립니다.";
        } else {
            recommendedColors = new Color[] { new Color(0, 0, 128), new Color(101, 67, 33), new Color(169, 169, 169), new Color(255, 255, 255) };
            colorNames = new String[] { "navy", "brown", "gray", "white" };
            description = "Black - 검정색은 중립적이고 고급스러운 느낌을 주는 색상으로 어두운 계열, 중립적인 색을 추천드립니다.";
        }
        
        // 추천 패널 생성
        ColorRecommendationPanel recommendationPanel = 
            new ColorRecommendationPanel(baseColor, recommendedColors,colorNames ,description);

        // 하단 패널에 추가 및 갱신
        bottomPanel.add(recommendationPanel, BorderLayout.CENTER);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }
}

class ColorRecommendationPanel extends JPanel {
    public ColorRecommendationPanel(Color baseColor, Color[] recommendedColors,String[] colorNames ,String description) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        
        JPanel recommendedPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        
        recommendedPanel.setBorder(null); // 테두리 제거
        recommendedPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // 왼쪽 여백 추가

        for (int i = 0; i < recommendedColors.length; i++) {
        	JPanel colorAndNamePanel = new JPanel(new BorderLayout());
            colorAndNamePanel.setOpaque(false); // 패널 배경 투명
            
            // 동그라미 패널
            RoundedPanel colorBox = new RoundedPanel(recommendedColors[i], 70);
            colorAndNamePanel.add(colorBox, BorderLayout.CENTER); // 중앙에 배치
            
            // 이름 레이블
            JLabel colorNameLabel = new JLabel(colorNames[i]);
            colorNameLabel.setFont(new Font("Serif", Font.BOLD, 15));
            colorNameLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
            colorNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // 여백 추가
            colorAndNamePanel.add(colorNameLabel, BorderLayout.SOUTH); // 아래에 배치

            recommendedPanel.add(colorAndNamePanel); // 전체 패널에 추가
        }
        
     

        JLabel descriptionLabel = new JLabel("<html>" + description + "</html>");
        descriptionLabel.setFont(new Font("Serif", Font.BOLD, 15)); // 글꼴 변경
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // 레이아웃에 추가
      
        add(recommendedPanel, BorderLayout.CENTER);
        add(descriptionLabel, BorderLayout.NORTH);
      
    }
}

class RoundedPanel extends JPanel {
    private int diameter;

    public RoundedPanel(Color color, int diameter) {
        this.diameter = diameter; // 동그라미 크기 설정
        setBackground(color); // 배경색 설정
        setOpaque(false); // 배경 투명 처리
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 동그라미 그리기
        g2.setColor(getBackground());
        g2.fillOval(0, 0, diameter, diameter);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(diameter, diameter); // 동그라미 크기 반환
    }
}

public class MyFrame extends JFrame{
	private UserFileManager UFM = new UserFileManager("./UserList.json");
	private ProductFileManager PFM = new ProductFileManager("./product.json");
	private ProductPanel currentImagePanel;
	private SearchPanel SearchP;
	private JPanel Panel;
	private List<Product> allProducts;
	private User user;
    private FirstPanel firstPanel;
    private BGMAudio audio;
    
	public MyFrame(BGMAudio audio, User user) {
		setTitle("J픽셀 유니버스");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (user != null) {
			System.out.println(user.getId());
		}
		Container c = getContentPane();
		//c.setLayout(new BorderLayout());
		this.user = user;
		this.audio = audio;
		List<Product> productList1 = PFM.readFromFile("상의", 0, 2);
		List<Product> productList2 = PFM.readFromFile("바지", 0, 2);
		List<Product> productList3 = PFM.readFromFile("아우터", 0, 1);
		List<Product> productList4 = PFM.readFromFile("기타", 0, 1);
		allProducts = new ArrayList<>();
		allProducts.addAll(productList1);
		allProducts.addAll(productList2);
		allProducts.addAll(productList3);
		allProducts.addAll(productList4);
		currentImagePanel = new ProductPanel(this, allProducts, user, false);
		SearchP = new SearchPanel(this);
		if(user == null) {
        	firstPanel = new FirstPanel(audio, this, user);
        }
        else {
			firstPanel = new FirstPanel(audio, this, user);
			firstPanel.onLoginSuccess(user);
		    System.out.println("MyFrame created with id: " + user.getId()); // 디버깅 로그
		    System.out.println(firstPanel.loggedInUser); // 디버깅 로그
        }
		Panel = new JPanel();
		Panel.setLayout(new BorderLayout());
		Panel.add(firstPanel, BorderLayout.NORTH);
		JPanel PanelCenter = new JPanel();
		PanelCenter.setLayout(new BoxLayout(PanelCenter, BoxLayout.Y_AXIS));
		SearchP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // 폭 무제한, 높이 30
		PanelCenter.add(SearchP);
		PanelCenter.add(new CategoryPanel(this));
		PanelCenter.add(currentImagePanel);
		Panel.add(PanelCenter, BorderLayout.CENTER);
		Panel.add(new ColorPanel(), BorderLayout.SOUTH);
		
		c.add(Panel);
		
		
		setSize(600, 790);
		setVisible(true);
	}
	// 처음 모습으로 변경
	public void changeProductPanel() {
		currentImagePanel = new ProductPanel(this, allProducts, user, false);
        // Panel의 기존 구성 제거 및 새로운 레이아웃 설정
        Panel.removeAll(); // Panel에 있는 모든 컴포넌트 제거
        Panel.setLayout(new BorderLayout());

        if (user != null)
        	firstPanel.onLoginSuccess(user);
		Panel.add(firstPanel, BorderLayout.NORTH);
		JPanel PanelCenter = new JPanel();
		PanelCenter.setLayout(new BoxLayout(PanelCenter, BoxLayout.Y_AXIS));
		PanelCenter.add(SearchP);
		PanelCenter.add(new CategoryPanel(this));
		PanelCenter.add(currentImagePanel);
		Panel.add(PanelCenter, BorderLayout.CENTER);
		Panel.add(new ColorPanel(), BorderLayout.SOUTH);
        // 컨테이너 변경 사항을 반영하도록 GUI 갱신
        getContentPane().validate();
        getContentPane().repaint();
    }
	// 장바구니용 패널 전환
	public void changeProductPanel(List<Product> list) {
		currentImagePanel = new ProductPanel(this, list, user, true);
        // Panel의 기존 구성 제거 및 새로운 레이아웃 설정
        Panel.removeAll(); // Panel에 있는 모든 컴포넌트 제거
        Panel.setLayout(new BorderLayout());
        JPanel BuyP = new JPanel();
        BuyP.setLayout(new FlowLayout(FlowLayout.RIGHT));
        BuyP.setPreferredSize(new Dimension(800, 20)); // 명시적으로 크기 설정
        int totalPrice = 0;
        if (!list.isEmpty()) {
        	for (Product p : list) { totalPrice += p.getPrice(); }
        }
        String userMoney = Integer.toString(user.getMoney());
        JLabel userMoneyLabel = new JLabel(user.getId() + "님의 보유 금액 = " + userMoney + "원\t\t\t|");
        JLabel totalPriceLabel = new JLabel("\tTotal = " + totalPrice + "원");
        JButton buybtn = new JButton("일괄 구매");
        buybtn.setBackground(Color.WHITE); // 로그인 버튼 배경색 흰색
        buybtn.addActionListener(new BuyAction(this, totalPrice));
        BuyP.add(userMoneyLabel);
        BuyP.add(totalPriceLabel);
        BuyP.add(buybtn);

		firstPanel.onLoginSuccess(user);
		Panel.add(firstPanel, BorderLayout.NORTH);
		JPanel PanelCenter = new JPanel();
		PanelCenter.setLayout(new BoxLayout(PanelCenter, BoxLayout.Y_AXIS));
		PanelCenter.add(BuyP);
		PanelCenter.add(currentImagePanel);
		//PanelCenter.add();
		Panel.add(PanelCenter, BorderLayout.CENTER);
		Panel.add(new ColorPanel(), BorderLayout.SOUTH);
        // 컨테이너 변경 사항을 반영하도록 GUI 갱신
        getContentPane().validate();
        getContentPane().repaint();
    }
	// Criterion에 맞는 결과로 변경
	public void changeProductPanel(String Criterion) {
		List<Product> newProducts = PFM.readFromFile(Criterion, 0, 6);
        currentImagePanel = new ProductPanel(this, newProducts, user, false);
        // Panel의 기존 구성 제거 및 새로운 레이아웃 설정
        Panel.removeAll(); // Panel에 있는 모든 컴포넌트 제거
        Panel.setLayout(new BorderLayout());

        if (user != null)
			firstPanel.onLoginSuccess(user);
		Panel.add(firstPanel, BorderLayout.NORTH);
		JPanel PanelCenter = new JPanel();
		PanelCenter.setLayout(new BoxLayout(PanelCenter, BoxLayout.Y_AXIS));
		PanelCenter.add(SearchP);
		PanelCenter.add(new CategoryPanel(this));
		PanelCenter.add(currentImagePanel);
		Panel.add(PanelCenter, BorderLayout.CENTER);
		Panel.add(new ColorPanel(), BorderLayout.SOUTH);
        // 컨테이너 변경 사항을 반영하도록 GUI 갱신
        getContentPane().validate();
        getContentPane().repaint();
    }
	class BuyAction implements ActionListener {
		private MyFrame mainFrame;
		private int price;
		BuyAction(MyFrame mainFrame, int price) {
			this.mainFrame = mainFrame;
			this.price = price;
		}
		@Override
		public void actionPerformed(ActionEvent e) { 
			int userMoney = user.getMoney();
			boolean isBuyed = false;
			if (userMoney < price) {
				System.out.println("돈이 부족합니다. 현재 보유한 금액: " + userMoney);
				return;
			}
			user.changeMoney(price, "-");
			Vector<String> cartItems = user.getcart();
			Vector<String> buyed = user.getbuyProduct();
			for (String item : cartItems) {
	        	for (String name : buyed) {
	        		if (name.equals(item)) { 
	        			System.out.println("재구매");
	        			isBuyed = true;
	        			break;
	        		}
	        	}
	        	if (!isBuyed) user.addbuyProduct(item);
	        	isBuyed = false;
			}
			user.resetcart();
			UFM.writeToFile(user);
    		List<Product> items = new ArrayList<Product>();
        	for (String name : cartItems) {
        		List<Product> searchProduct = PFM.readFromFile(name, 0, 1);
        		items.addAll(searchProduct);
        	}
        	mainFrame.changeProductPanel(items);
		}
	}
	public ProductFileManager getFileManager() { return PFM; }
	public BGMAudio getAudio() { return audio; }
	public static void main(String[] args) {
		MyFrame frame = new MyFrame(new BGMAudio(), null);
		// 창 닫을 때 리소스 정리
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	frame.getAudio().stopAudio(); // 창 닫힐 때 오디오 정지 및 해제
            }
        });
	}
}