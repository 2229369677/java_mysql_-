package com.student.gui;

import com.student.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.security.MessageDigest;

public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private boolean registered = false;
    
    public RegisterDialog(Window parent) {
        super(parent, "用户注册", ModalityType.APPLICATION_MODAL);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));
        
        // 添加组件
        add(new JLabel("用户名:"));
        usernameField = new JTextField();
        add(usernameField);
        
        add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        add(passwordField);
        
        add(new JLabel("确认密码:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);
        
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> registerUser());
        add(registerButton);
        
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }
    
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致");
            return;
        }
        
        try {
            String hashedPassword = md5(password);
            
        try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "注册成功");
                    registered = true;
                    dispose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "注册失败: " + e.getMessage());
        }
    }
    
    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    
    public boolean isRegistered() {
        return registered;
    }
}
