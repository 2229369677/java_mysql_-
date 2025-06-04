package com.student.gui;

import com.student.model.Student;
import com.student.service.StudentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

public class SearchDialog extends JDialog {
    private StudentService studentService;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;

    public SearchDialog(JFrame parent) {
        super(parent, "查找学生", true);
        this.studentService = new StudentService();
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search criteria panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("搜索类型:"));
        
        String[] searchTypes = {"学号", "姓名", "专业"};
        searchTypeComboBox = new JComboBox<>(searchTypes);
        searchPanel.add(searchTypeComboBox);
        
        searchPanel.add(new JLabel("搜索内容:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(this::performSearch);
        searchPanel.add(searchButton);

        // Results table
        String[] columnNames = {"ID", "学号", "姓名", "性别", "出生日期", "专业", "班级"};
        tableModel = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void performSearch(ActionEvent e) {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入搜索内容", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String searchType = (String) searchTypeComboBox.getSelectedItem();
        List<Student> results = null;

        switch (searchType) {
            case "学号":
                Student student = studentService.getStudentByStudentNo(searchText);
                if (student != null) results = Collections.singletonList(student);
                break;
            case "姓名":
                results = studentService.getStudentsByName(searchText);
                break;
            case "专业":
                results = studentService.getStudentsByMajor(searchText);
                break;
        }

        updateResultsTable(results);
    }

    private void updateResultsTable(List<Student> students) {
        tableModel.setRowCount(0); // Clear table
        
        if (students == null || students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "未找到匹配的学生", "结果", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Student student : students) {
            Object[] rowData = {
                student.getId(),
                student.getStudentNo(),
                student.getName(),
                student.getGender(),
                student.getBirthDate(),
                student.getMajor(),
                student.getClassName()
            };
            tableModel.addRow(rowData);
        }
    }
}
