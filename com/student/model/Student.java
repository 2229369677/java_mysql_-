package com.student.model;

import java.time.LocalDate;

/**
 * 学生实体类 - 核心数据模型
 * 
 * 功能:
 * 1. 映射数据库中的学生表结构
 * 2. 在DAO层、服务层和UI层之间传输学生数据
 * 3. 封装学生的所有属性和基本行为
 * 
 * 关联关系:
 * - 被StudentDAO类用于数据库操作
 * - 被StudentService类用于业务逻辑处理
 * - 被各种GUI对话框用于界面展示
 */
public class Student {
    // 学生ID（数据库主键，自增长字段）
    // 由数据库自动生成，添加新学生时通常为0
    private int id;
    
    // 学号（唯一标识符，不能重复）
    // 在注册时由用户输入，用于登录和查询
    private String studentNo;
    
    // 学生姓名
    private String name;
    
    // 性别（通常存储"男"或"女"）
    private String gender;
    
    // 出生日期（使用Java 8的LocalDate类型）
    // 格式：YYYY-MM-DD
    private LocalDate birthDate;
    
    // 专业（如：计算机科学、电子工程等）
    private String major;
    
    // 班级（如：CS-101）
    private String className;
    
    // 联系电话（11位手机号）
    private String phone;
    
    // 电子邮箱
    private String email;
    
    // 住址
    private String address;
    
    /**
     * 无参构造函数
     * 
     * 使用场景:
     * 1. 框架反射创建对象时
     * 2. 需要手动设置属性的情况
     * 3. 作为DTO传输时的空对象创建
     */
    public Student() {
    }
    
    /**
     * 全参构造函数（不包含ID）
     * 
     * 使用场景:
     * 1. 创建新学生记录时（ID由数据库自动生成）
     * 2. 界面输入数据到对象的转换
     * 
     * @param studentNo 学号（唯一标识）
     * @param name 姓名
     * @param gender 性别
     * @param birthDate 出生日期
     * @param major 专业
     * @param className 班级
     * @param phone 联系电话
     * @param email 电子邮箱
     * @param address 住址
     */
    public Student(String studentNo, String name, String gender, LocalDate birthDate, 
                   String major, String className, String phone, String email, String address) {
        this.studentNo = studentNo;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.major = major;
        this.className = className;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    // ======================
    // Getter和Setter方法
    // ======================
    
    /**
     * 注意：这些方法被以下场景调用
     * 1. DAO层：从ResultSet中填充对象属性
     * 2. 服务层：业务逻辑处理中访问属性
     * 3. GUI层：在界面表格中显示数据
     */
    
    /**
     * 获取学生ID
     * @return 学生ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * 设置学生ID
     * @param id 学生ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * 获取学号
     * @return 学号
     */
    public String getStudentNo() {
        return studentNo;
    }
    
    /**
     * 设置学号
     * @param studentNo 学号
     */
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    
    /**
     * 获取姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置姓名
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取性别
     * @return 性别
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * 设置性别
     * @param gender 性别
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * 获取出生日期
     * @return 出生日期
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    /**
     * 设置出生日期
     * @param birthDate 出生日期
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    /**
     * 获取专业
     * @return 专业
     */
    public String getMajor() {
        return major;
    }
    
    /**
     * 设置专业
     * @param major 专业
     */
    public void setMajor(String major) {
        this.major = major;
    }
    
    /**
     * 获取班级
     * @return 班级
     */
    public String getClassName() {
        return className;
    }
    
    /**
     * 设置班级
     * @param className 班级
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
     * 获取电话号码
     * @return 电话号码
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * 设置电话号码
     * @param phone 电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * 获取邮箱
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 设置邮箱
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 获取地址
     * @return 地址
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * 设置地址
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * 重写toString方法
     * 
     * 功能:
     * 1. 提供对象的文本表示形式
     * 2. 用于日志记录和调试输出
     * 3. 在控制台应用程序中显示学生信息
     * 
     * 调用场景:
     * - StudentSearchConsole中显示查询结果
     * - 服务层和DAO层的调试日志
     * 
     * @return 格式化的学生信息字符串
     */
    @Override
    public String toString() {
        return "学生信息{" +
                "ID=" + id +
                ", 学号='" + studentNo + '\'' +
                ", 姓名='" + name + '\'' +
                ", 性别='" + gender + '\'' +
                ", 出生日期=" + birthDate +
                ", 专业='" + major + '\'' +
                ", 班级='" + className + '\'' +
                ", 电话='" + phone + '\'' +
                ", 邮箱='" + email + '\'' +
                ", 地址='" + address + '\'' +
                '}';
    }
}
