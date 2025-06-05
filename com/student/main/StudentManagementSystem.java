package com.student.main;

import com.student.gui.LoginDialog;
import com.student.gui.MainFrame;
import com.student.model.Student;
import com.student.service.StudentService;
import com.student.util.DatabaseConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * 学生管理系统主入口 - 支持GUI和控制台双模式
 * 
 * 功能:
 * 1. 提供基于Swing的图形用户界面(GUI)
 * 2. 提供基于控制台的命令行界面(CLI)
 * 3. 两种模式共享相同的业务逻辑层(StudentService)
 * 
 * 设计特点:
 * - GUI模式: 使用LoginDialog进行身份验证，通过后显示MainFrame
 * - CLI模式: 提供全功能的控制台菜单系统
 * - 启动时自动测试数据库连接
 * 
 * 使用说明:
 * 1. 默认启动GUI模式
 * 2. 如需使用CLI模式，注释掉main方法中的GUI代码，取消注释CLI代码
 */
public class StudentManagementSystem {
    // 学生服务对象（业务逻辑层）
    private StudentService studentService;
    
    // 控制台输入扫描器
    private Scanner scanner;
    
    // 日期格式化器（统一日期格式）
    private DateTimeFormatter dateFormatter;

    /**
     * 构造函数
     * 
     * 初始化:
     * 1. 创建学生服务实例
     * 2. 准备控制台输入扫描器
     * 3. 设置日期格式化器（yyyy-MM-dd）
     */
    public StudentManagementSystem() {
        this.studentService = new StudentService();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // ISO标准格式
    }

    /**
     * 主入口方法
     * 
     * 默认启动流程:
     * 1. 显示登录对话框
     * 2. 验证用户凭证
     * 3. 登录成功显示主界面，失败则退出
     * 
     * 切换命令行模式:
     * 注释GUI代码块，取消注释命令行代码块
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // ==================== GUI模式 ====================
        // 创建登录对话框
        LoginDialog loginDialog = new LoginDialog(null);
        loginDialog.setVisible(true);
        
        // 验证登录结果
        if (loginDialog.isAuthenticated()) {
            // 登录成功，显示主界面
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            // 登录失败或取消，退出系统
            System.exit(0);
        }

        // ==================== 命令行模式 ====================
        // 如需使用命令行模式，取消注释以下代码并注释上面的GUI代码
        /*
        StudentManagementSystem studentManagementSystem = new StudentManagementSystem();
        studentManagementSystem.run();
        */
    }

    /**
     * 运行命令行模式主程序
     * 
     * 流程:
     * 1. 显示欢迎信息
     * 2. 测试数据库连接
     * 3. 进入主菜单循环
     * 4. 根据用户选择执行操作
     * 5. 提供错误处理和用户友好的提示
     */
    public void run() {
        System.out.println("=================================================");
        System.out.println("           欢迎使用学生管理系统（命令行版）");
        System.out.println("=================================================");

        // 测试数据库连接（系统启动必要检查）
        if (!DatabaseConnection.testConnection()) {
            System.out.println("数据库连接失败，请检查数据库配置！");
            System.out.println("1. 确保MySQL服务已启动");
            System.out.println("2. 检查config.properties中的连接参数");
            return;
        }

        boolean running = true;
        while (running) {
            try {
                showMainMenu(); // 显示主菜单
                int choice = getIntInput("请选择操作");

                // 根据用户选择调用相应功能
                switch (choice) {
                    case 1: addStudent(); break;          // 添加学生
                    case 2: deleteStudent(); break;       // 删除学生
                    case 3: updateStudent(); break;       // 更新学生
                    case 4: queryStudentById(); break;    // ID查询
                    case 5: queryStudentByStudentNo(); break; // 学号查询
                    case 6: queryStudentsByName(); break; // 姓名查询
                    case 7: queryStudentsByMajor(); break; // 专业查询
                    case 8: showAllStudents(); break;     // 显示所有学生
                    case 0:                               // 退出系统
                        running = false;
                        System.out.println("感谢使用学生管理系统，再见！");
                        break;
                    default:
                        System.out.println("无效的选择，请重新输入！");
                }

                // 每次操作后暂停，让用户看到结果
                if (running) {
                    System.out.println("\n按回车键继续...");
                    scanner.nextLine(); // 等待用户确认
                }
            } catch (Exception e) {
                // 异常处理：显示错误并返回主菜单
                System.out.println("发生错误: " + e.getMessage());
                System.out.println("按回车键返回主菜单...");
                scanner.nextLine();
            }
        }

        scanner.close(); // 关闭扫描器释放资源
    }

    /**
     * 显示控制台主菜单
     * 
     * 菜单设计:
     * - 清晰的功能分区
     * - 简洁的选项编号
     * - 提供退出选项
     */
    private void showMainMenu() {
        System.out.println("\n=================================================");
        System.out.println("                 主菜单");
        System.out.println("=================================================");
        System.out.println("1. 添加学生信息");
        System.out.println("2. 删除学生信息");
        System.out.println("3. 修改学生信息");
        System.out.println("4. 根据ID查询学生");
        System.out.println("5. 根据学号查询学生");
        System.out.println("6. 根据姓名查询学生");
        System.out.println("7. 根据专业查询学生");
        System.out.println("8. 显示所有学生");
        System.out.println("0. 退出系统");
        System.out.println("=================================================");
    }

    /**
     * 添加学生流程
     * 
     * 流程:
     * 1. 显示添加学生标题
     * 2. 循环收集学生信息（可中途取消）
     * 3. 验证并提交到服务层
     * 4. 显示操作结果
     * 
     * 设计特点:
     * - 每个字段提供取消选项（输入0返回）
     * - 可选字段可以留空
     * - 实时反馈验证结果
     */
    private void addStudent() {
        System.out.println("\n==================== 添加学生信息 ====================");

        try {
            while (true) {
                // 逐步获取学生信息（每个步骤都可取消）
                String studentNo = getStringInput("学号 (输入0返回主菜单)");
                if (studentNo.equals("0")) return;

                String name = getStringInput("姓名 (输入0返回主菜单)");
                if (name.equals("0")) return;

                String gender = getGenderInput();
                if (gender.equals("0")) return;

                LocalDate birthDate = getDateInput("出生日期 (格式: yyyy-MM-dd, 输入0返回主菜单)");
                if (birthDate == null) return; // 用户输入0会返回null

                String major = getStringInput("专业 (输入0返回主菜单)");
                if (major.equals("0")) return;

                String className = getStringInput("班级 (输入0返回主菜单)");
                if (className.equals("0")) return;

                String phone = getOptionalStringInput("电话号码 (可选, 输入0返回主菜单)");
                if (phone != null && phone.equals("0")) return;

                String email = getOptionalStringInput("邮箱 (可选, 输入0返回主菜单)");
                if (email != null && email.equals("0")) return;

                String address = getOptionalStringInput("地址 (可选, 输入0返回主菜单)");
                if (address != null && address.equals("0")) return;

                // 创建学生对象
                Student student = new Student(studentNo, name, gender, birthDate, major, className, phone, email, address);

                // 调用服务层添加学生
                if (studentService.addStudent(student)) {
                    System.out.println("学生信息添加成功！");
                    break; // 添加成功退出循环
                } else {
                    System.out.println("学生信息添加失败，请检查输入！");
                    // 失败时继续循环，允许用户重新输入
                }
            }
        } catch (Exception e) {
            System.out.println("添加学生信息时发生错误: " + e.getMessage());
        }
    }

    /**
     * 删除学生信息
     */
    private void deleteStudent() {
        System.out.println("\n==================== 删除学生信息 ====================");

        int id = getIntInput("请输入要删除的学生ID");

        // 先查询学生信息
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("未找到ID为 " + id + " 的学生信息！");
            return;
        }

        // 显示学生信息
        studentService.printStudentInfo(student);

        // 确认删除
        String confirm = getStringInput("确认删除该学生信息吗？(y/n)");
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            if (studentService.deleteStudent(id)) {
                System.out.println("学生信息删除成功！");
            } else {
                System.out.println("学生信息删除失败！");
            }
        } else {
            System.out.println("取消删除操作。");
        }
    }

    /**
     * 修改学生信息
     */
    private void updateStudent() {
        System.out.println("\n==================== 修改学生信息 ====================");

        int id = getIntInput("请输入要修改的学生ID");

        // 先查询学生信息
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("未找到ID为 " + id + " 的学生信息！");
            return;
        }

        // 显示当前学生信息
        System.out.println("当前学生信息：");
        studentService.printStudentInfo(student);

        try {
            System.out.println("\n请输入新的学生信息（直接回车保持原值）：");

            String studentNo = getStringInputWithDefault("学号", student.getStudentNo());
            String name = getStringInputWithDefault("姓名", student.getName());
            String gender = getGenderInputWithDefault(student.getGender());
            LocalDate birthDate = getDateInputWithDefault("出生日期 (格式: yyyy-MM-dd)", student.getBirthDate());
            String major = getStringInputWithDefault("专业", student.getMajor());
            String className = getStringInputWithDefault("班级", student.getClassName());
            String phone = getOptionalStringInputWithDefault("电话号码", student.getPhone());
            String email = getOptionalStringInputWithDefault("邮箱", student.getEmail());
            String address = getOptionalStringInputWithDefault("地址", student.getAddress());

            // 更新学生信息
            student.setStudentNo(studentNo);
            student.setName(name);
            student.setGender(gender);
            student.setBirthDate(birthDate);
            student.setMajor(major);
            student.setClassName(className);
            student.setPhone(phone);
            student.setEmail(email);
            student.setAddress(address);

            if (studentService.updateStudent(student)) {
                System.out.println("学生信息修改成功！");
            } else {
                System.out.println("学生信息修改失败！");
            }

        } catch (Exception e) {
            System.out.println("修改学生信息时发生错误: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询学生
     */
    private void queryStudentById() {
        System.out.println("\n==================== 根据ID查询学生 ====================");

        int id = getIntInput("请输入学生ID");
        Student student = studentService.getStudentById(id);
        studentService.printStudentInfo(student);
    }

    /**
     * 根据学号查询学生
     */
    private void queryStudentByStudentNo() {
        System.out.println("\n==================== 根据学号查询学生 ====================");

        String studentNo = getStringInput("请输入学号");
        Student student = studentService.getStudentByStudentNo(studentNo);
        studentService.printStudentInfo(student);
    }

    /**
     * 根据姓名查询学生
     */
    private void queryStudentsByName() {
        System.out.println("\n==================== 根据姓名查询学生 ====================");

        String name = getStringInput("请输入姓名关键字");
        List<Student> students = studentService.getStudentsByName(name);
        studentService.printStudentList(students);
    }

    /**
     * 根据专业查询学生
     */
    private void queryStudentsByMajor() {
        System.out.println("\n==================== 根据专业查询学生 ====================");

        String major = getStringInput("请输入专业名称");
        List<Student> students = studentService.getStudentsByMajor(major);
        studentService.printStudentList(students);
    }

    /**
     * 显示所有学生
     */
    private void showAllStudents() {
        System.out.println("\n==================== 所有学生信息 ====================");

        List<Student> students = studentService.getAllStudents();
        studentService.printStudentList(students);
    }

    /**
     * 获取字符串输入
     * @param prompt 提示信息
     * @return 用户输入的字符串
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * 获取可选字符串输入
     * @param prompt 提示信息
     * @return 用户输入的字符串，可能为null
     */
    private String getOptionalStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    /**
     * 获取带默认值的字符串输入
     * @param prompt 提示信息
     * @param defaultValue 默认值
     * @return 用户输入的字符串或默认值
     */
    private String getStringInputWithDefault(String prompt, String defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    /**
     * 获取带默认值的可选字符串输入
     * @param prompt 提示信息
     * @param defaultValue 默认值
     * @return 用户输入的字符串或默认值
     */
    private String getOptionalStringInputWithDefault(String prompt, String defaultValue) {
        System.out.print(prompt + " [" + (defaultValue != null ? defaultValue : "无") + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        return input.equals("无") ? null : input;
    }

    /**
     * 获取整数输入
     * @param prompt 提示信息
     * @return 用户输入的整数
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            }
        }
    }

    /**
     * 获取性别输入
     * @return 性别字符串
     */
    private String getGenderInput() {
        while (true) {
            String gender = getStringInput("性别 (男/女)");
            if (gender.equals("男") || gender.equals("女")) {
                return gender;
            }
            System.out.println("性别只能输入'男'或'女'！");
        }
    }

    /**
     * 获取带默认值的性别输入
     * @param defaultValue 默认值
     * @return 性别字符串
     */
    private String getGenderInputWithDefault(String defaultValue) {
        while (true) {
            System.out.print("性别 (男/女) [" + defaultValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            if (input.equals("男") || input.equals("女")) {
                return input;
            }
            System.out.println("性别只能输入'男'或'女'！");
        }
    }

    /**
     * 获取日期输入
     * @param prompt 提示信息
     * @return 日期对象，如果用户输入0则返回null
     */
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                String dateStr = getStringInput(prompt);
                if (dateStr.equals("0")) return null;
                return LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式不正确，请使用 yyyy-MM-dd 格式！");
            }
        }
    }

    /**
     * 获取带默认值的日期输入
     * @param prompt 提示信息
     * @param defaultValue 默认值
     * @return 日期对象
     */
    private LocalDate getDateInputWithDefault(String prompt, LocalDate defaultValue) {
        while (true) {
            try {
                System.out.print(prompt + " [" + defaultValue.format(dateFormatter) + "]: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return defaultValue;
                }
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式不正确，请使用 yyyy-MM-dd 格式！");
            }
        }
    }
}
