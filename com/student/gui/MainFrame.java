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

    /**
     * 主框架构造函数
     * 功能：初始化学生服务，设置窗口属性，创建UI并加载学生数据
     * 调用方式：由StudentManagementSystem在登录成功后调用
     * 依赖关系：调用initUI()和loadStudentData()
     */
    public MainFrame() {
        // 创建学生服务对象，用于业务逻辑处理
        studentService = new StudentService();
        
        // 设置窗口标题和大小
        setTitle("学生管理系统");
        setSize(800, 600);
        
        // 设置窗口关闭行为
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 窗口居中显示
        setLocationRelativeTo(null);

        // 初始化用户界面组件
        initUI();
        
        // 加载学生数据到表格
        loadStudentData();
    }

    /**
     * 初始化用户界面
     * 功能：创建搜索面板、表格、按钮面板等UI组件，并设置事件监听器
     * 调用方式：由构造函数调用，用于构建主界面
     * 依赖关系：调用showAddStudentDialog(), deleteSelectedStudent(), loadStudentData(), 
     *           searchStudentsByName(), searchStudentByNo(), sortStudents()
     */
    private void initUI() {
        // 创建搜索面板，使用流式布局（左对齐）
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // 搜索条件标签
        JLabel searchLabel = new JLabel("搜索条件:");
        // 搜索类型下拉框（姓名/学号）
        String[] searchOptions = {"姓名", "学号"};
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchOptions);
        // 搜索输入框
        JTextField searchField = new JTextField(20);
        // 搜索按钮
        JButton searchButton = new JButton("搜索");
        // 清除按钮
        JButton clearButton = new JButton("清除");
        
        // 排序相关组件
        JLabel sortLabel = new JLabel("排序方式:");
        String[] sortOptions = {"学号升序", "学号降序"};
        JComboBox<String> sortCombo = new JComboBox<>(sortOptions);
        JButton sortButton = new JButton("排序");
        
        // 将搜索组件添加到搜索面板
        searchPanel.add(searchLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        searchPanel.add(sortLabel);
        searchPanel.add(sortCombo);
        searchPanel.add(sortButton);

        // 创建表格模型（学号, 姓名, 年龄, 性别, 专业）
        String[] columnNames = {"学号", "姓名", "年龄", "性别", "专业"};
        tableModel = new DefaultTableModel(columnNames, 0);
        // 创建表格并添加滚动面板
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加学生");
        JButton deleteButton = new JButton("删除学生");
        JButton refreshButton = new JButton("刷新");
        /**
         * 添加查找按钮
         * */
        JButton searchButton1 = new JButton("查找学生");
        buttonPanel.add(searchButton1);
        searchButton1.addActionListener(e -> {
            SearchDialog searchDialog = new SearchDialog(this);
            searchDialog.setVisible(true);
        });

        // 添加学生按钮：点击打开添加学生对话框
        addButton.addActionListener(e -> showAddStudentDialog());
        // 删除学生按钮：点击删除选中学生
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        // 刷新按钮：重新加载学生数据
        refreshButton.addActionListener(e -> loadStudentData());
        
        // 搜索按钮事件处理
        searchButton.addActionListener(e -> {
            // 获取选中的搜索类型
            String searchType = (String) searchTypeCombo.getSelectedItem();
            // 获取搜索关键词
            String searchTerm = searchField.getText().trim();
            
            // 验证搜索内容
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入搜索内容");
                return;
            }
            
            // 根据搜索类型调用不同搜索方法
            if ("姓名".equals(searchType)) {
                searchStudentsByName(searchTerm);
            } else if ("学号".equals(searchType)) {
                searchStudentByNo(searchTerm);
            }
        });
        
        // 清除按钮事件处理
        clearButton.addActionListener(e -> {
            // 清空搜索框
            searchField.setText("");
            // 重新加载所有学生数据
            loadStudentData();
        });
        
        // 排序按钮事件处理
        sortButton.addActionListener(e -> {
            // 获取选中的排序类型
            String sortType = (String) sortCombo.getSelectedItem();
            // 执行排序
            sortStudents(sortType);
        });

        // 将按钮添加到按钮面板
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 设置主布局（边界布局）
        setLayout(new BorderLayout());
        // 搜索面板放在顶部
        add(searchPanel, BorderLayout.NORTH);
        // 表格放在中间
        add(scrollPane, BorderLayout.CENTER);
        // 按钮面板放在底部
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 加载学生数据
     * 功能：从服务层获取所有学生数据，计算年龄并显示在表格中
     * 调用方式：初始化界面时调用，也可通过"刷新"按钮触发
     * 依赖关系：调用studentService.getAllStudents()获取数据
     *          调用updateTableWithStudents()更新表格
     */
    void loadStudentData() {
        // 清空表格现有数据
        tableModel.setRowCount(0);
        
        // 从服务层获取所有学生数据
        List<Student> students = studentService.getAllStudents();
        
        // 遍历学生列表，添加到表格
        for (Student student : students) {
            // 计算学生年龄（根据出生日期）
            int age = Period.between(student.getBirthDate(), LocalDate.now()).getYears();
            
            // 创建表格行数据
            Object[] rowData = {
                student.getStudentNo(),  // 学号
                student.getName(),       // 姓名
                age,                     // 年龄
                student.getGender(),     // 性别
                student.getMajor()       // 专业
            };
            
            // 添加行到表格模型
            tableModel.addRow(rowData);
        }
    }

    /**
     * 显示添加学生对话框
     * 功能：创建并显示添加学生的对话框
     * 调用方式：点击"添加学生"按钮时触发
     */
    private void showAddStudentDialog() {
        AddStudentDialog dialog = new AddStudentDialog(this);
        dialog.setVisible(true);
    }

    /**
     * 删除选中学生
     * 功能：删除表格中当前选中的学生记录
     * 调用方式：点击"删除学生"按钮时触发
     * 依赖关系：调用studentService.deleteStudentByNo()执行删除
     *          调用loadStudentData()刷新表格
     */
    private void deleteSelectedStudent() {
        // 获取当前选中的行
        int selectedRow = studentTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            // 从表格中获取学号和姓名
            String studentNo = (String) tableModel.getValueAt(selectedRow, 0);
            String studentName = (String) tableModel.getValueAt(selectedRow, 1);
            
            // 显示确认对话框
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除学生 " + studentName + " (" + studentNo + ") 吗?",
                "确认删除",
                JOptionPane.YES_NO_OPTION
            );
            
            // 如果用户确认删除
            if (confirm == JOptionPane.YES_OPTION) {
                // 调用服务层删除学生
                if (studentService.deleteStudentByNo(studentNo)) {
                    JOptionPane.showMessageDialog(this, "删除成功");
                    // 刷新表格数据
                    loadStudentData();
                } else {
                    JOptionPane.showMessageDialog(this, "删除失败");
                }
            }
        } else {
            // 没有选中行时提示
            JOptionPane.showMessageDialog(this, "请先选择学生");
        }
    }
    
    /**
     * 按姓名搜索学生
     * 功能：根据姓名搜索匹配的学生并显示结果
     * @param name 要搜索的学生姓名
     * 调用方式：在搜索面板选择"姓名"并点击搜索按钮时触发
     */
    private void searchStudentsByName(String name) {
        List<Student> students = studentService.getStudentsByName(name);
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "未找到姓名为 '" + name + "' 的学生");
            return;
        }
        updateTableWithStudents(students);
    }
    
    /**
     * 按学号搜索学生
     * 功能：根据学号精确查找学生并显示结果
     * @param studentNo 要搜索的学生学号
     * 调用方式：在搜索面板选择"学号"并点击搜索按钮时触发
     */
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
    
    /**
     * 用学生列表更新表格
     * 功能：清空表格并用提供的学生列表重新填充
     * @param students 要显示的学生列表
     * 调用方式：由loadStudentData、searchStudentsByName、searchStudentByNo和sortStudents方法调用
     */
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
    
    /**
     * 按学号排序学生
     * 功能：对学生列表按学号进行升序或降序排序并更新表格
     * @param sortType 排序类型："学号升序"或"学号降序"
     * 调用方式：点击"排序"按钮时触发
     * 依赖关系：调用studentService.getAllStudents()获取数据
     *          调用updateTableWithStudents()更新表格
     */
    private void sortStudents(String sortType) {
        // 获取所有学生数据
        List<Student> students = studentService.getAllStudents();
        
        // 使用比较器按学号排序
        students.sort((s1, s2) -> {
            if ("学号升序".equals(sortType)) {
                // 升序：s1学号小于s2学号时返回负数
                return s1.getStudentNo().compareTo(s2.getStudentNo());
            } else {
                // 降序：s2学号小于s1学号时返回负数
                return s2.getStudentNo().compareTo(s1.getStudentNo());
            }
        });
        
        // 用排序后的列表更新表格
        updateTableWithStudents(students);
    }

    // Main method removed - application now launched via StudentManagementSystem
}
