package com.student.gui;

import com.student.model.Student;
import com.student.service.StudentService;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    private StudentService studentService;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public MainFrame() {
        studentService = new StudentService();
        setTitle("学生管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadStudentData();
    }

    private void initUI() {
        // 表格模型
        String[] columnNames = {"学号", "姓名", "年龄", "性别", "专业"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加学生");
        JButton deleteButton = new JButton("删除学生");
        JButton refreshButton = new JButton("刷新");

        addButton.addActionListener(e -> showAddStudentDialog());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        refreshButton.addActionListener(e -> loadStudentData());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 布局
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    void loadStudentData() {
        tableModel.setRowCount(0); // 清空表格
        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            // 计算年龄
            int age = Period.between(student.getBirthDate(), LocalDate.now()).getYears();
            
            Object[] rowData = {
                student.getStudentNo(),
                student.getName(),
                age,
                student.getGender(),
                student.getMajor()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddStudentDialog() {
        AddStudentDialog dialog = new AddStudentDialog(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentNo = (String) tableModel.getValueAt(selectedRow, 0);
            studentService.deleteStudentByNo(studentNo);
            loadStudentData();
        } else {
            JOptionPane.showMessageDialog(this, "请先选择学生");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
