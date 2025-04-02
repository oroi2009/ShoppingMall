package ShoppingMall;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
	
public class UserInfoFrame extends JFrame {
	private BGMAudio audioP;
	UserInfoFrame(BGMAudio audioP, FirstPanel firstPanel, String Userid){
		setTitle("J픽셀 유니버스");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
        setLocationRelativeTo(null); // 기본적으로 중앙에 띄움
	    System.out.println("회원정보User id: " + Userid); // 디버깅 로그
	    this.audioP = audioP;
	    
	    UserFileManager fileManager = new UserFileManager("./UserList.json"); // 기존의 UserFileManager를 FileManager로 수정
        User user = fileManager.readFromFile(Userid, 0, Integer.MAX_VALUE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 40, 40)); 

        // 상단 패널 생성 (뒤로가기 버튼 포함)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(40, 40, 40)); 

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.setBackground(new Color(100, 149, 237)); 
        backButton.setForeground(Color.WHITE);
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(135, 206, 250)); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(100, 149, 237));
            }
        });

        // 뒤로가기 버튼의 액션 리스너
		backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	 int x = getX();
	             int y = getY();

	             // MyFrame 객체 생성 후 위치 설정
	             MyFrame myFrame = new MyFrame(audioP, user);
	             myFrame.setLocation(x, y); // 현재 창 위치로 설정
	             myFrame.setVisible(true);
	            
	             // 현재 LoginFrame 닫기
	             dispose();
	           }
	    }); 
		
		topPanel.add(backButton);
        panel.add(topPanel);
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // 컴포넌트 간 간격 설정
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(40, 40, 40)); // 배경색 설
        
        // ID 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(Color.WHITE); // 흰색 텍스트
        inputPanel.add(idLabel, gbc);

        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel idLabel2 = new JLabel(user.getId());
        idLabel2.setForeground(Color.WHITE); // 흰색 텍스트
        inputPanel.add(idLabel2, gbc);

        // 전화번호 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel telLabel = new JLabel("전화번호:");
        telLabel.setForeground(Color.WHITE);
        inputPanel.add(telLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String telNumber = user.getTel();
        String formattedTelNumber = telNumber.substring(0, 3) + "-" + telNumber.substring(3, 7) + "-" + telNumber.substring(7);
        JLabel telLabel2 = new JLabel(formattedTelNumber);
        telLabel2.setForeground(Color.WHITE);
        inputPanel.add(telLabel2, gbc);
        
        // 주소 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel addressLabel = new JLabel("주소:");
        addressLabel.setForeground(Color.WHITE);
        inputPanel.add(addressLabel, gbc);
 
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel addressLabel2 = new JLabel(user.getAddress());
        addressLabel2.setForeground(Color.WHITE);
        inputPanel.add(addressLabel2, gbc);
        
        // 잔액 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel moneyLabel = new JLabel("잔액:");
        moneyLabel.setForeground(Color.WHITE);
        inputPanel.add(moneyLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel moneyLabel2 = new JLabel(String.valueOf(user.getMoney()));
        moneyLabel2.setForeground(Color.WHITE);
        inputPanel.add(moneyLabel2, gbc);

        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간 간격 설정
        
        //충전 버튼
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH; // 버튼을 셀 크기에 맞게 확장
        JButton moneyButton = new JButton("충전");
        moneyButton.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
        moneyButton.setForeground(Color.WHITE); // 텍스트 색상 흰색

        inputPanel.add(moneyButton, gbc);
        
        moneyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	moneyButton.setBackground(new Color(135, 206, 250)); // 마우스 올리면 색상 변경 (light sky blue)
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	moneyButton.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
            }
        });
        
        moneyButton.addActionListener(e -> {
            // 다이얼로그 생성
            JDialog chargeDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(moneyButton), "충전하기", true);
            chargeDialog.setSize(350, 200);
            chargeDialog.setLayout(new GridBagLayout());
            chargeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            // 컴포넌트 설정
            JLabel label = new JLabel("충전 금액:");
            JTextField amountField = new JTextField(10);
            JButton submitButton = new JButton("확인");
            JButton cancelButton = new JButton("취소");
            
            Color buttonBackground = new Color(40, 40, 40); 
            Color buttonTextColor = Color.WHITE; 

            submitButton.setBackground(buttonBackground);
            submitButton.setForeground(buttonTextColor);

            cancelButton.setBackground(buttonBackground);
            cancelButton.setForeground(buttonTextColor);

            // Layout 배치
            GridBagConstraints gbc_2 = new GridBagConstraints();
            gbc_2.insets = new Insets(10, 10, 10, 10); // 패딩 추가
            
            gbc_2.gridx = 0;
            gbc_2.gridy = 0;
            chargeDialog.add(label, gbc_2);

            gbc_2.gridx = 1;
            gbc_2.gridy = 0;
            chargeDialog.add(amountField, gbc_2);

            gbc_2.gridx = 0;
            gbc_2.gridy = 1;
            gbc_2.gridwidth = 2;
            gbc_2.fill = GridBagConstraints.HORIZONTAL;
            chargeDialog.add(submitButton, gbc_2);

            gbc_2.gridy = 2;
            chargeDialog.add(cancelButton, gbc_2);

            // 이벤트 처리
            submitButton.addActionListener(ev -> {
                String amountText = amountField.getText();
                try {
                    int amount = Integer.parseInt(amountText);
                    if (amount > 0 && amount <= 200000) {
                    	user.changeMoney(amount, "+");
                    	fileManager.writeToFile(user);
                        JOptionPane.showMessageDialog(chargeDialog, "잔액이 " + amount + "원 충전되었습니다.");
                        chargeDialog.dispose(); // 다이얼로그 닫기
                        moneyLabel2.setText(Integer.toString(user.getMoney()));
                        repaint();
                    } else if (amount > 200000) {
                        JOptionPane.showMessageDialog(chargeDialog, "충전 금액은 20만 원을 초과할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(chargeDialog, "유효하지 않은 금액입니다. 다시 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(chargeDialog, "숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(ev -> chargeDialog.dispose());

            // 다이얼로그 표시
            chargeDialog.setLocationRelativeTo(moneyButton); // 버튼 기준 중앙에 위치
            chargeDialog.setVisible(true);
        });

        // 로그아웃 버튼
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 2; // 두 줄(ID와 PS 필드) 차지
        gbc.anchor = GridBagConstraints.EAST;
        JButton logoutButton = new JButton("로그아웃");
        logoutButton.setPreferredSize(new Dimension(50, 30));
        logoutButton.setBackground(new Color(100, 149, 237)); 
        logoutButton.setForeground(Color.WHITE); 

        inputPanel.add(logoutButton, gbc);
        
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	logoutButton.setBackground(new Color(135, 206, 250)); // 마우스 올리면 색상 변경 (light sky blue)
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	logoutButton.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = getX();
	            int y = getY();

	            // MyFrame 객체 생성 후 위치 설정
	            MyFrame myFrame = new MyFrame(audioP, null);
	            myFrame.setLocation(x, y); // 현재 창 위치로 설정
	            myFrame.setVisible(true);
	            // 현재 LoginFrame 닫기
	            dispose();
			}
		});
        
       
        
        panel.add(inputPanel);
        add(panel);

	}

}
