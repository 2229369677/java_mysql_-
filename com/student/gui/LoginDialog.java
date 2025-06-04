package com.student.gui;

import com.student.util.DatabaseConnection;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import java.security.MessageDigest;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean authenticated = false;
    
    public LoginDialog(JFrame parent) {
        super(parent, "学生管理系统登录", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
    setLayout(new GridLayout(4, 2, 10, 10));
        
        // 添加组件
        add(new JLabel("用户名:"));
        usernameField = new JTextField();
        add(usernameField);
        
        add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        add(passwordField);
        
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> authenticate());
    add(loginButton);
    
    // 添加注册按钮
    JButton registerButton = new JButton("注册");
    registerButton.addActionListener(e -> {
        RegisterDialog registerDialog = new RegisterDialog(LoginDialog.this);
        registerDialog.setVisible(true);
    });
    add(registerButton);
    
    JButton cancelButton = new JButton("取消");
    cancelButton.addActionListener(e -> dispose());
    add(cancelButton);
    }
    
    private void authenticate() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String inputHashedPassword = md5(password); // 对输入密码进行哈希
                if (inputHashedPassword.equals(storedHashedPassword)) {
                    authenticated = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "密码错误");
                }
            } else {
                JOptionPane.showMessageDialog(this, "用户不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库连接失败: " + e.getMessage());
        }
    }
    
    // MD5哈希方法（与RegisterDialog中相同）
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "密码加密失败: " + e.getMessage());
            return ""; // 返回空字符串会确保认证失败
        }
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
}
