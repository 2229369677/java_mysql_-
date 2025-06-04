package com.student.gui;

import com.student.model.Student;
import com.student.service.StudentService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeParseException;

public class AddStudentDialog extends JDialog {
    private StudentService studentService;
    private MainFrame mainFrame;
    private JTextField studentNoField;
    private JTextField nameField;
    private JComboBox<String> genderComboBox;
    private JTextField birthDateField;
    private JTextField majorField;
    private JTextField classNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AddStudentDialog(MainFrame parent) {
        super(parent, "添加学生", true);
        this.mainFrame = parent;
        this.studentService = new StudentService();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 学号
        panel.add(new JLabel("学号:"));
        studentNoField = new JTextField();
        panel.add(studentNoField);
        
        // 姓名
        panel.add(new JLabel("姓名:"));
        nameField = new JTextField();
        panel.add(nameField);
        
        // 性别
        panel.add(new JLabel("性别:"));
        genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        panel.add(genderComboBox);
        
        // 出生日期
        panel.add(new JLabel("出生日期 (yyyy-MM-dd):"));
        birthDateField = new JTextField();
        panel.add(birthDateField);
        
        // 专业
        panel.add(new JLabel("专业:"));
        majorField = new JTextField();
        panel.add(majorField);
        
        // 班级
        panel.add(new JLabel("班级:"));
        classNameField = new JTextField();
        panel.add(classNameField);
        
        // 电话
        panel.add(new JLabel("电话:"));
        phoneField = new JTextField();
        panel.add(phoneField);
        
        // 邮箱
        panel.add(new JLabel("邮箱:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        // 地址
        panel.add(new JLabel("地址:"));
        addressField = new JTextField();
        panel.add(addressField);
        
        // 按钮
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveStudent());
        
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveStudent() {
        try {
            String studentNo = studentNoField.getText().trim();
            String name = nameField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            LocalDate birthDate = parseDate(birthDateField.getText().trim());
            String major = majorField.getText().trim();
            String className = classNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            
            // 验证必填字段
            if (studentNo.isEmpty() || name.isEmpty() || birthDate == null || major.isEmpty() || className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写所有必填字段", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 创建学生对象
            Student student = new Student(studentNo, name, gender, birthDate, major, className, 
                                          phone.isEmpty() ? null : phone, 
                                          email.isEmpty() ? null : email, 
                                          address.isEmpty() ? null : address);
            
            // 保存学生
            if (studentService.addStudent(student)) {
                JOptionPane.showMessageDialog(this, "学生添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.loadStudentData(); // 刷新主界面数据
                dispose(); // 关闭对话框
            } else {
                JOptionPane.showMessageDialog(this, "添加学生失败，请检查学号是否重复", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "保存时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "日期格式不正确，请使用 yyyy-MM-dd 格式", "错误", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
