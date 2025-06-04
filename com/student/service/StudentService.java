package com.student.service;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * 学生管理服务类
 * 负责学生管理的业务逻辑处理
 */
public class StudentService {
    private StudentDAO studentDAO;
    
    /**
     * 构造函数
     */
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }
    
    /**
     * 添加学生信息
     * @param student 学生对象
     * @return boolean 添加是否成功
     */
    public boolean addStudent(Student student) {
        // 验证学生信息
        if (!validateStudent(student)) {
            return false;
        }
        
        // 检查学号是否已存在
        if (studentDAO.isStudentNoExists(student.getStudentNo())) {
            System.out.println("错误：学号 " + student.getStudentNo() + " 已存在！");
            return false;
        }
        
        return studentDAO.addStudent(student);
    }
    
    /**
     * 删除学生信息
     * @param id 学生ID
     * @return boolean 删除是否成功
     */
    public boolean deleteStudent(int id) {
        if (id <= 0) {
            System.out.println("错误：学生ID无效！");
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
     * @param student 学生对象
     * @return boolean 更新是否成功
     */
    public boolean updateStudent(Student student) {
        // 验证学生信息
        if (!validateStudent(student)) {
            return false;
        }
        
        if (student.getId() <= 0) {
            System.out.println("错误：学生ID无效！");
            return false;
        }
        
        // 检查学号是否被其他学生使用
        Student existingStudent = studentDAO.getStudentByStudentNo(student.getStudentNo());
        if (existingStudent != null && existingStudent.getId() != student.getId()) {
            System.out.println("错误：学号 " + student.getStudentNo() + " 已被其他学生使用！");
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
     * 验证学生信息
     * @param student 学生对象
     * @return boolean 验证是否通过
     */
    private boolean validateStudent(Student student) {
        if (student == null) {
            System.out.println("错误：学生信息不能为空！");
            return false;
        }
        
        // 验证学号
        if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
            System.out.println("错误：学号不能为空！");
            return false;
        }
        
        // 验证姓名
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            System.out.println("错误：姓名不能为空！");
            return false;
        }
        
        // 验证性别
        if (student.getGender() == null || student.getGender().trim().isEmpty()) {
            System.out.println("错误：性别不能为空！");
            return false;
        }
        
        if (!student.getGender().equals("男") && !student.getGender().equals("女")) {
            System.out.println("错误：性别只能是'男'或'女'！");
            return false;
        }
        
        // 验证出生日期
        if (student.getBirthDate() == null) {
            System.out.println("错误：出生日期不能为空！");
            return false;
        }
        
        // 检查出生日期是否合理（不能是未来日期）
        if (student.getBirthDate().isAfter(LocalDate.now())) {
            System.out.println("错误：出生日期不能是未来日期！");
            return false;
        }
        
        // 验证专业
        if (student.getMajor() == null || student.getMajor().trim().isEmpty()) {
            System.out.println("错误：专业不能为空！");
            return false;
        }
        
        // 验证班级
        if (student.getClassName() == null || student.getClassName().trim().isEmpty()) {
            System.out.println("错误：班级不能为空！");
            return false;
        }
        
        // 验证电话号码格式（简单验证）
        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            String phone = student.getPhone().trim();
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                System.out.println("错误：电话号码格式不正确！");
                return false;
            }
        }
        
        // 验证邮箱格式（简单验证）
        if (student.getEmail() != null && !student.getEmail().trim().isEmpty()) {
            String email = student.getEmail().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("错误：邮箱格式不正确！");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 打印学生信息列表
     * @param students 学生列表
     */
    public void printStudentList(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("没有找到学生信息。");
            return;
        }
        
        System.out.println("\n==================== 学生信息列表 ====================");
        System.out.printf("%-5s %-12s %-10s %-4s %-12s %-15s %-10s %-15s %-20s %-20s%n",
                "ID", "学号", "姓名", "性别", "出生日期", "专业", "班级", "电话", "邮箱", "地址");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        
        for (Student student : students) {
            System.out.printf("%-5d %-12s %-10s %-4s %-12s %-15s %-10s %-15s %-20s %-20s%n",
                    student.getId(),
                    student.getStudentNo(),
                    student.getName(),
                    student.getGender(),
                    student.getBirthDate(),
                    student.getMajor(),
                    student.getClassName(),
                    student.getPhone() != null ? student.getPhone() : "",
                    student.getEmail() != null ? student.getEmail() : "",
                    student.getAddress() != null ? student.getAddress() : "");
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.println("共找到 " + students.size() + " 条学生信息。");
    }
    
    /**
     * 打印单个学生信息
     * @param student 学生对象
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
        System.out.println("电话: " + (student.getPhone() != null ? student.getPhone() : "未填写"));
        System.out.println("邮箱: " + (student.getEmail() != null ? student.getEmail() : "未填写"));
        System.out.println("地址: " + (student.getAddress() != null ? student.getAddress() : "未填写"));
        System.out.println("====================================================");
    }
}
