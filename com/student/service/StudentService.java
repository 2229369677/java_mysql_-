package com.student.service;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * 学生服务层 - 业务逻辑实现
 * 
 * 职责:
 * 1. 封装学生管理核心业务逻辑
 * 2. 提供验证、数据转换、事务管理等服务
 * 3. 作为DAO层和UI层的中间层，实现解耦
 * 
 * 设计特点:
 * - 业务逻辑集中处理（如验证规则）
 * - 事务管理边界（当前每个方法独立事务）
 * - 异常处理策略（捕获并记录，不向上传播）
 * 
 * 关联关系:
 * - 依赖StudentDAO进行数据访问
 * - 被控制台应用和GUI应用调用
 * - 操作Student模型对象
 */
public class StudentService {
    // 数据访问对象（采用组合而非继承）
    private StudentDAO studentDAO;
    
    /**
     * 构造函数
     * 
     * 初始化:
     * 1. 创建StudentDAO实例
     * 2. 建立服务层与数据访问层的关联
     */
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * 添加学生信息（业务逻辑入口）
     * 
     * 流程:
     * 1. 验证学生对象有效性
     * 2. 检查学号唯一性
     * 3. 调用DAO执行添加操作
     * 
     * 业务规则:
     * - 学号必须唯一
     * - 所有必填字段必须符合格式要求
     * 
     * @param student 包含学生信息的对象
     * @return 添加成功返回true，验证失败或数据库操作失败返回false
     */
    public boolean addStudent(Student student) {
        // 执行领域模型验证
        if (!validateStudent(student)) {
            return false;
        }
        
        // 业务规则：学号唯一性检查
        if (studentDAO.isStudentNoExists(student.getStudentNo())) {
            System.out.println("业务错误：学号 " + student.getStudentNo() + " 已存在！");
            return false;
        }
        
        // 委托给DAO执行数据库操作
        return studentDAO.addStudent(student);
    }
    
    /**
     * 删除学生记录
     * 
     * 流程:
     * 1. 验证ID有效性
     * 2. 调用DAO执行删除操作
     * 
     * 注意:
     * - 执行前不检查记录是否存在（由DAO处理）
     * 
     * @param id 学生表主键ID
     * @return 删除成功返回true，无效ID或操作失败返回false
     */
    public boolean deleteStudent(int id) {
        // 业务规则：ID必须为正整数
        if (id <= 0) {
            System.out.println("业务错误：学生ID无效！");
            return false;
        }
        
        return studentDAO.deleteStudent(id);
    }
    
    /**
     * 根据学号删除学生
     * @param studentNo 学号
     * @return 删除是否成功
     */
    public boolean deleteStudentByNo(String studentNo) {
        if (studentNo == null || studentNo.trim().isEmpty()) {
            System.out.println("错误：学号不能为空！");
            return false;
        }
        return studentDAO.deleteByStudentNo(studentNo);
    }
    
    /**
     * 更新学生信息
     * 
     * 流程:
     * 1. 验证学生对象有效性
     * 2. 验证ID有效性
     * 3. 检查学号更新冲突
     * 4. 调用DAO执行更新
     * 
     * 业务规则:
     * - 学号更新时不能与其他学生冲突
     * - 更新后的信息必须通过所有验证规则
     * 
     * @param student 包含更新后信息的学生对象（必须包含有效ID）
     * @return 更新成功返回true，验证失败或操作失败返回false
     */
    public boolean updateStudent(Student student) {
        // 领域模型验证
        if (!validateStudent(student)) {
            return false;
        }
        
        // 业务规则：ID必须有效
        if (student.getId() <= 0) {
            System.out.println("业务错误：学生ID无效！");
            return false;
        }
        
        // 业务规则：学号更新冲突检查
        Student existingStudent = studentDAO.getStudentByStudentNo(student.getStudentNo());
        if (existingStudent != null && existingStudent.getId() != student.getId()) {
            System.out.println("业务错误：学号 " + student.getStudentNo() + " 已被其他学生使用！");
            return false;
        }
        
        return studentDAO.updateStudent(student);
    }
    
    /**
     * 根据ID查询学生信息
     * @param id 学生ID
     * @return Student 学生对象
     */
    public Student getStudentById(int id) {
        if (id <= 0) {
            System.out.println("错误：学生ID无效！");
            return null;
        }
        
        return studentDAO.getStudentById(id);
    }
    
    /**
     * 根据学号查询学生信息
     * @param studentNo 学号
     * @return Student 学生对象
     */
    public Student getStudentByStudentNo(String studentNo) {
        if (studentNo == null || studentNo.trim().isEmpty()) {
            System.out.println("错误：学号不能为空！");
            return null;
        }
        
        return studentDAO.getStudentByStudentNo(studentNo.trim());
    }
    
    /**
     * 根据姓名模糊查询学生信息
     * @param name 姓名关键字
     * @return List<Student> 学生列表
     */
    public List<Student> getStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("错误：姓名不能为空！");
            return null;
        }
        
        return studentDAO.getStudentsByName(name.trim());
    }
    
    /**
     * 获取所有学生信息
     * @return List<Student> 所有学生列表
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
    
    /**
     * 根据专业查询学生信息
     * @param major 专业名称
     * @return List<Student> 该专业的学生列表
     */
    public List<Student> getStudentsByMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            System.out.println("错误：专业名称不能为空！");
            return null;
        }
        
        return studentDAO.getStudentsByMajor(major.trim());
    }
    
    /**
     * 学生领域模型验证（核心业务规则）
     * 
     * 功能:
     * 1. 验证学生对象的完整性和业务规则
     * 2. 提供详细的错误反馈
     * 
     * 验证规则:
     * 1. 对象非空
     * 2. 必填字段非空（学号、姓名、性别、出生日期、专业、班级）
     * 3. 性别只能是"男"或"女"
     * 4. 出生日期不能是未来日期
     * 5. 电话号码格式（可选但需符合格式）
     * 6. 邮箱格式（可选但需符合格式）
     * 
     * @param student 待验证的学生对象
     * @return 通过验证返回true，否则false
     */
    private boolean validateStudent(Student student) {
        // 1. 整体对象检查
        if (student == null) {
            System.out.println("验证失败：学生信息不能为空！");
            return false;
        }
        
        // 2. 学号验证（必填）
        if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
            System.out.println("验证失败：学号不能为空！");
            return false;
        }
        
        // 3. 姓名验证（必填）
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("验证失败：姓名不能为空！");
            return false;
        }
        
        // 4. 性别验证（必填且有限值）
        if (student.getGender() == null || student.getGender().trim().isEmpty()) {
            System.out.println("验证失败：性别不能为空！");
            return false;
        }
        String gender = student.getGender().trim();
        if (!gender.equals("男") && !gender.equals("女")) {
            System.out.println("验证失败：性别只能是'男'或'女'！");
            return false;
        }
        
        // 5. 出生日期验证（必填且合理）
        if (student.getBirthDate() == null) {
            System.out.println("验证失败：出生日期不能为空！");
            return false;
        }
        if (student.getBirthDate().isAfter(LocalDate.now())) {
            System.out.println("验证失败：出生日期不能是未来日期！");
            return false;
        }
        
        // 6. 专业验证（必填）
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            System.out.println("验证失败：专业不能为空！");
            return false;
        }
        
        // 7. 班级验证（必填）
        if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
            System.out.println("验证失败：班级不能为空！");
            return false;
        }
        
        // 8. 电话号码格式验证（可选）
        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            String phone = student.getPhone().trim();
            // 中国大陆手机号简单验证（1开头，11位）
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                System.out.println("验证失败：电话号码格式不正确（应为11位中国大陆手机号）！");
                return false;
            }
        }
        
        // 9. 邮箱格式验证（可选）
        if (student.getEmail() != null && !student.getEmail().trim().isEmpty()) {
            String email = student.getEmail().trim();
            // 基础邮箱格式验证
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("验证失败：邮箱格式不正确！");
                return false;
            }
        }
        
        // 所有验证通过
        return true;
    }
    
    /**
     * 打印学生列表（控制台专用）
     * 
     * 功能:
     * 1. 以表格形式展示学生列表
     * 2. 处理空值和空列表情况
     * 3. 格式化日期和空值显示
     * 
     * 设计说明:
     * - 专为控制台应用设计
     * - 使用固定宽度列实现表格效果
     * - 支持大量数据分页（未实现）
     * 
     * @param students 要展示的学生列表
     */
    public void printStudentList(List<Student> students) {
        // 空列表处理
        if (students == null || students.isEmpty()) {
            System.out.println("没有找到学生信息。");
            return;
        }
        
        // 打印表头
        System.out.println("\n==================== 学生信息列表 ====================");
        System.out.printf("%-5s %-12s %-10s %-4s %-12s %-15s %-10s %-15s %-20s %-20s%n",
                "ID", "学号", "姓名", "性别", "出生日期", "专业", "班级", "电话", "邮箱", "地址");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        
        // 遍历并打印每条记录
        for (Student student : students) {
            System.out.printf("%-5d %-12s %-10s %-4s %-12s %-15s %-10s %-15s %-20s %-20s%n",
                    student.getId(),
                    student.getStudentNo(),
                    student.getName(),
                    student.getGender(),
                    student.getBirthDate(), // LocalDate默认格式YYYY-MM-DD
                    student.getMajor(),
                    student.getClassName(),
                    student.getPhone() != null ? student.getPhone() : "N/A",
                    student.getEmail() != null ? student.getEmail() : "N/A",
                    student.getAddress() != null ? student.getAddress() : "N/A");
        }
        
        // 打印表尾和统计信息
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.println("共找到 " + students.size() + " 条学生信息。");
    }
    
    /**
     * 打印单个学生详情（控制台专用）
     * 
     * 功能:
     * 1. 以易读格式展示学生详情
     * 2. 处理空值显示
     * 3. 提供清晰的分隔和标题
     * 
     * @param student 要展示的学生对象
     */
    public void printStudentInfo(Student student) {
        if (student == null) {
            System.out.println("学生信息不存在。");
            return;
        }
        
        System.out.println("\n==================== 学生详细信息 ====================");
        System.out.println("ID: " + student.getId());
        System.out.println("学号: " + student.getStudentNo());
        System.out.println("姓名: " + student.getName());
        System.out.println("性别: " + student.getGender());
        System.out.println("出生日期: " + student.getBirthDate());
        System.out.println("专业: " + student.getMajor());
        System.out.println("班级: " + student.getClassName());
        System.out.println("电话: " + (student.getPhone() != null ? student.getPhone() : "未提供"));
        System.out.println("邮箱: " + (student.getEmail() != null ? student.getEmail() : "未提供"));
        System.out.println("地址: " + (student.getAddress() != null ? student.getAddress() : "未提供"));
        System.out.println("====================================================");
    }
}
