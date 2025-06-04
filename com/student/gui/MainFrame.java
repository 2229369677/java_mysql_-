package com.student.gui;

import com.student.model.Student;
import com.student.service.StudentService;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//searchDialog
import com.student.gui.AddStudentDialog;
import com.student.gui.SearchDialog;

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
        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("搜索条件:");
        String[] searchOptions = {"姓名", "学号"};
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchOptions);
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        JButton clearButton = new JButton("清除");
        
        // 添加排序选项
        JLabel sortLabel = new JLabel("排序方式:");
        String[] sortOptions = {"学号升序", "学号降序"};
        JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
        JButton sortButton = new JButton("排序");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(sortLabel);
        searchPanel.add(sortCombo);
        searchPanel.add(sortButton);

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
        
        // 搜索按钮事件
        searchButton.addActionListener(e -> {
            String searchType = (String) searchTypeCombo.getSelectedItem();
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入搜索内容");
                return;
            }
            
            if ("姓名".equals(searchType)) {
                searchStudentsByName(searchTerm);
            } else if ("学号".equals(searchType)) {
                searchStudentByNo(searchTerm);
            }
        });
        
        // 清除按钮事件
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadStudentData();
        });
        
        // 排序按钮事件
        sortButton.addActionListener(e -> {
            String sortType = (String) sortCombo.getSelectedItem();
            sortStudents(sortType);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 布局
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
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
            String studentName = (String) tableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除学生 " + studentName + " (" + studentNo + ") 吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (studentService.deleteStudentByNo(studentNo)) {
                    JOptionPane.showMessageDialog(this, "删除成功");
                    loadStudentData();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "请先选择学生");
        }
    }
    
    private void searchStudentsByName(String name) {
        List<Student> students = studentService.getStudentsByName(name);
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "未找到姓名为 '" + name + "' 的学生");
            return;
        }
        updateTableWithStudents(students);
    }
    
    private void searchStudentByNo(String studentNo) {
        Student student = studentService.getStudentByStudentNo(studentNo);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "未找到学号为 '" + studentNo + "' 的学生");
            return;
        }
        List<Student> students = new ArrayList<>();
        students.add(student);
        updateTableWithStudents(students);
    }
    
    private void updateTableWithStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
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
    
    // 按学号排序学生
    private void sortStudents(String sortType) {
        List<Student> students = studentService.getAllStudents();
        
        // 使用比较器按学号排序
        students.sort((s1, s2) -> {
            if ("学号升序".equals(sortType)) {
                return s1.getStudentNo().compareTo(s2.getStudentNo());
            } else {
                return s2.getStudentNo().compareTo(s1.getStudentNo());
            }
        });
        
        updateTableWithStudents(students);
    }

    // Main method removed - application now launched via StudentManagementSystem
}
