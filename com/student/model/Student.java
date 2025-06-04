package com.student.model;

import java.time.LocalDate;

/**
 * 学生实体类
 * 用于表示学生的基本信息
 */
public class Student {
    private int id;              // 学生ID（主键）
    private String studentNo;    // 学号
    private String name;         // 姓名
    private String gender;       // 性别
    private LocalDate birthDate; // 出生日期
    private String major;        // 专业
    private String className;    // 班级
    private String phone;        // 电话号码
    private String email;        // 邮箱
    private String address;      // 地址
    
    /**
     * 无参构造函数
     */
    public Student() {
    }
    
    /**
     * 带参构造函数
     * @param studentNo 学号
     * @param name 姓名
     * @param gender 性别
     * @param birthDate 出生日期
     * @param major 专业
     * @param className 班级
     * @param phone 电话号码
     * @param email 邮箱
     * @param address 地址
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
    
    // Getter和Setter方法
    
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
     * 重写toString方法，用于打印学生信息
     * @return 学生信息字符串
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
