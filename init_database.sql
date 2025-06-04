-- 创建学生管理数据库
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- 创建学生表
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '学生ID',
    student_no VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('男', '女') NOT NULL COMMENT '性别',
    birth_date DATE NOT NULL COMMENT '出生日期',
    major VARCHAR(50) NOT NULL COMMENT '专业',
    class_name VARCHAR(50) NOT NULL COMMENT '班级',
    phone VARCHAR(15) COMMENT '电话号码',
    email VARCHAR(50) COMMENT '邮箱',
    address VARCHAR(100) COMMENT '地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 添加索引
CREATE INDEX idx_student_no ON students(student_no);
CREATE INDEX idx_name ON students(name);
CREATE INDEX idx_major ON students(major);

-- 插入示例数据
INSERT INTO students (student_no, name, gender, birth_date, major, class_name, phone, email, address) 
VALUES 
('2023001', '张三', '男', '2000-05-15', '计算机科学', '计科1班', '13800138000', 'zhangsan@example.com', '北京市海淀区'),
('2023002', '李四', '女', '2001-08-22', '软件工程', '软件2班', '13900139000', 'lisi@example.com', '上海市浦东新区'),
('2023003', '王五', '男', '1999-12-10', '人工智能', '智能1班', '13700137000', 'wangwu@example.com', '广州市天河区');

-- 查询验证
SELECT * FROM students;
