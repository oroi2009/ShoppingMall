package ShoppingMall;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SignupFrame extends JFrame {
    SignupFrame(LoginFrame LoginFrame) {
        setTitle("회원가입");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // 메인 패널
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setBackground(new Color(40, 40, 40));

        // 상단 패널 생성 (뒤로가기 버튼 포함)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout으로 변경
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

        // 뒤로가기 버튼 액션 리스너
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 현재 프레임 닫기
                LoginFrame.setVisible(true); // 로그인 프레임 보이기
            }
        });

        
        topPanel.add(backButton);
        panel.add(topPanel);


        // GridBagLayout을 사용한 입력 항목
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(40, 40, 40)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // 컴포넌트 간 간격

        // ID 라벨 및 텍스트 필드
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(Color.WHITE);
        JTextField idField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(idField, gbc);

        // 비밀번호 라벨 및 텍스트 필드
        JLabel psLabel = new JLabel("PS:");
        psLabel.setForeground(Color.WHITE);
        JPasswordField psField = new JPasswordField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(psLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(psField, gbc);
        
        // 전화번호 라벨 및 텍스트 필드
        JLabel phoneLabel = new JLabel("전화번호:");
        phoneLabel.setForeground(Color.WHITE);
        JTextField phoneField = new JTextField(15);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(phoneField, gbc);
        
        // 주소 라벨 및 텍스트 필드
        JLabel addressLabel = new JLabel("주소:");
        addressLabel.setForeground(Color.WHITE);
        JTextField addressField = new JTextField(15);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(addressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(addressField, gbc);
        
        panel.add(inputPanel);

        // 회원가입 버튼 (중앙 정렬)
        JButton signupButton = new JButton("Sign up");
        signupButton.setBackground(new Color(100, 149, 237));
        signupButton.setForeground(Color.WHITE);

        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(new Color(135, 206, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(new Color(100, 149, 237));
            }
        });
        
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(psField.getPassword());
                String phone = phoneField.getText();
                String address = addressField.getText();

                if (id.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    // 모든 필드가 입력되었는지 확인
                    JOptionPane.showMessageDialog(null, "모든 필드를 채워주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // UserFileManager 생성
                UserFileManager fileManager = new UserFileManager("./UserList.json");

                // ID 중복 확인
                if (fileManager.readFromFile(id, 0, Integer.MAX_VALUE) != null) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.length() != 11 || !phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "전화번호는 11자리 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // User 객체 생성
                User newUser = new User(id, password, phone, address, 0, false, null,null);

                // 파일에 데이터 저장
                fileManager.writeToFile(newUser);
                JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);

                // 회원가입 창 닫고 로그인 창으로 이동
                dispose();
                LoginFrame.setVisible(true);
            }
        });
        
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        signupPanel.setBackground(new Color(40, 40, 40)); 
        signupPanel.add(signupButton);
        panel.add(signupPanel);
        
        


        // 패널 추가
        add(panel);
    }
}