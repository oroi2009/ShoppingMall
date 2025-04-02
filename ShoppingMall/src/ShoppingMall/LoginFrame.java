package ShoppingMall;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    SignupFrame sf;
	LoginFrame(){
		setTitle("J픽셀 유니버스");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
        setLocationRelativeTo(null); 
        
        sf = new SignupFrame(this);

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
	             MyFrame myFrame = new MyFrame(new BGMAudio(), null);
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
        inputPanel.setBackground(new Color(40, 40, 40)); 
        
        // ID 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(Color.WHITE); 
        inputPanel.add(idLabel, gbc);

        // ID 입력 텍스트 필드
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField idField = new JTextField(15); // 입력 필드 너비
        inputPanel.add(idField, gbc);

        // Password 텍스트 라벨
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel psLabel = new JLabel("PS:");
        psLabel.setForeground(Color.WHITE);
        inputPanel.add(psLabel, gbc);

        // Password 입력 텍스트 필드
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField psField = new JPasswordField(15);
        inputPanel.add(psField, gbc);

        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간 간격 설정
        
        // 로그인 버튼
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2; // 두 줄(ID와 PS 필드) 차지
        gbc.fill = GridBagConstraints.BOTH; // 버튼을 셀 크기에 맞게 확장
        JButton loginButton = new JButton("로그인");
        loginButton.setBackground(new Color(100, 149, 237)); 
        loginButton.setForeground(Color.WHITE); 

        inputPanel.add(loginButton, gbc);

        // 회원가입 버튼
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JButton signupButton = new JButton("회원가입");
        signupButton.setPreferredSize(new Dimension(50, 30));
        signupButton.setBackground(new Color(100, 149, 237)); 
        signupButton.setForeground(Color.WHITE); 

        inputPanel.add(signupButton, gbc);
        
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	loginButton.setBackground(new Color(135, 206, 250)); // 마우스 올리면 색상 변경 (light sky blue)
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	loginButton.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
            }
        });
        
        // 로그인 버튼의 동작 추가
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = idField.getText();
                String inputPassword = new String(psField.getPassword());

                if (inputID.isEmpty() || inputPassword.isEmpty()) {
                    return; // 텍스트 필드가 비어 있으면 아무 작업도 하지 않음
                }
                
                UserFileManager fileManager = new UserFileManager("./UserList.json"); // 기존의 UserFileManager를 FileManager로 수정
                User user = fileManager.readFromFile(inputID, 0, Integer.MAX_VALUE);

                if (user == null) {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 ID입니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 비밀번호 검증
                if (!user.getPassword().equals(inputPassword)) {
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

              
                // 로그인 성공
                int x = getX();
                int y = getY();
                MyFrame myFrame = new MyFrame(new BGMAudio(), user);
                myFrame.setLocation(x, y);
                myFrame.setVisible(true);
                dispose(); // 현재 창 닫기
            } 
            
        });
        
        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	signupButton.setBackground(new Color(135, 206, 250)); // 마우스 올리면 색상 변경 (light sky blue)
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	signupButton.setBackground(new Color(100, 149, 237)); // 진한 파란색 (Cornflower Blue)
            }
        });
        
        signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// FirstPanel의 위치를 얻어 로그인 창에 전달
                int x = getX();
                int y = getY();

                // 로그인 창을 해당 위치로 띄우기
                sf.setLocation(x, y);
				sf.setVisible(true);
	
		        dispose(); // 부모 창 닫기
			}
		});
        
        // 패널을 프레임에 추가
        panel.add(inputPanel);
        add(panel);
       
	
	}
}