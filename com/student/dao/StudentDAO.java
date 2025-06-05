package com.student.dao;

import com.student.model.Student;
import com.student.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据访问对象（DAO） - CRUD操作实现
 * 
 * 职责:
 * 1. 封装对学生表的所有数据库操作
 * 2. 实现CRUD（创建、读取、更新、删除）功能
 * 3. 提供查询接口供服务层调用
 * 
 * 设计模式:
 * - 数据访问对象模式（DAO）
 * - 使用PreparedStatement防止SQL注入
 * - 资源自动关闭（try-with-resources）
 * 
 * 关联关系:
 * - 依赖DatabaseConnection获取数据库连接
 * - 被StudentService调用执行业务逻辑
 * - 操作Student模型对象
 * 
 * 事务管理:
 * - 每个方法独立事务（自动提交）
 * - 复杂操作需在Service层管理事务
 */
public class StudentDAO {
    
    /**
     * 添加学生记录到数据库
     * 
     * 流程:
     * 1. 准备带参数的SQL语句
     * 2. 从连接池获取连接
     * 3. 设置学生对象属性到PreparedStatement
     * 4. 执行更新操作
     * 5. 验证影响行数
     * 
     * 注意:
     * - 学号(student_no)必须唯一，调用前应通过isStudentNoExists()验证
     * - 使用try-with-resources自动关闭资源
     * 
     * @param student 包含学生信息的对象（ID应为0，由数据库自增生成）
     * @return 添加成功返回true，失败返回false
     */
    public boolean addStudent(Student student) {
        // 使用参数化查询防止SQL注入
        String sql = "INSERT INTO students (student_no, name, gender, birth_date, major, class_name, phone, email, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 绑定参数（注意顺序与SQL中的占位符一致）
            statement.setString(1, student.getStudentNo());      // 学号
            statement.setString(2, student.getName());           // 姓名
            statement.setString(3, student.getGender());         // 性别
            statement.setDate(4, Date.valueOf(student.getBirthDate())); // 出生日期（转换为SQL Date）
            statement.setString(5, student.getMajor());          // 专业
            statement.setString(6, student.getClassName());      // 班级
            statement.setString(7, student.getPhone());          // 电话
            statement.setString(8, student.getEmail());          // 邮箱
            statement.setString(9, student.getAddress());        // 地址
            
            // 执行插入并获取影响行数
            int rowsAffected = statement.executeUpdate();
            
            // 验证操作结果
            if (rowsAffected > 0) {
                System.out.println("学生信息添加成功！学号: " + student.getStudentNo());
                return true;
            }
            
        } catch (SQLException e) {
            // 捕获并记录具体错误
            System.err.println("添加学生信息时发生错误: " + e.getMessage());
            // 实际项目应使用日志框架记录完整堆栈
        }
        
        return false;
    }
    
    /**
     * 根据ID删除学生记录
     * 
     * 流程:
     * 1. 准备带ID参数的删除语句
     * 2. 设置学生ID参数
     * 3. 执行删除操作
     * 
     * 注意:
     * - 删除操作不可逆
     * - 应先通过getStudentById()验证记录存在
     * 
     * @param id 学生表主键ID
     * @return 删除成功返回true，未找到记录返回false
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 设置要删除的学生ID
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("成功删除ID为 " + id + " 的学生信息");
                return true;
            } else {
                System.out.println("删除失败：未找到ID为 " + id + " 的学生记录");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("删除学生信息时发生错误: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 更新学生信息
     * 
     * 流程:
     * 1. 准备带参数的更新语句
     * 2. 设置学生对象的所有属性（包括ID）
     * 3. 执行更新操作
     * 
     * 注意:
     * - 将更新所有字段（全量更新）
     * - 学号(student_no)更新时需确保唯一性
     * 
     * @param student 包含更新后信息的学生对象（必须包含有效ID）
     * @return 更新成功返回true，未找到记录返回false
     */
    public boolean updateStudent(Student student) {
        // 更新所有字段的SQL语句
        String sql = "UPDATE students SET student_no = ?, name = ?, gender = ?, birth_date = ?, major = ?, class_name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 绑定更新参数（注意顺序）
            statement.setString(1, student.getStudentNo());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender());
            statement.setDate(4, Date.valueOf(student.getBirthDate()));
            statement.setString(5, student.getMajor());
            statement.setString(6, student.getClassName());
            statement.setString(7, student.getPhone());
            statement.setString(8, student.getEmail());
            statement.setString(9, student.getAddress());
            statement.setInt(10, student.getId()); // WHERE条件
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("成功更新学生信息: ID=" + student.getId() + ", 学号=" + student.getStudentNo());
                return true;
            } else {
                System.out.println("更新失败：未找到ID为 " + student.getId() + " 的学生记录");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("更新学生信息时发生错误: " + e.getMessage());
            return false;
        }
    }
    
    
    /**
     * 根据学号删除学生
     * @param studentNo 学号
     * @return 删除是否成功
     */
    public boolean deleteByStudentNo(String studentNo) {
        String sql = "DELETE FROM students WHERE student_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentNo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("根据学号删除学生时出错: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据学号查询学生信息
     * @param studentNo 学号
     * @return Student 学生对象，如果未找到返回null
     */
    public Student getStudentByStudentNo(String studentNo) {
        String sql = "SELECT * FROM students WHERE student_no = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, studentNo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractStudentFromResultSet(resultSet);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("查询学生信息时发生错误: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 根据姓名模糊查询学生信息
     * @param name 姓名关键字
     * @return List<Student> 学生列表
     */
    public List<Student> getStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, "%" + name + "%");
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(extractStudentFromResultSet(resultSet));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("查询学生信息时发生错误: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * 获取所有学生信息
     * @return List<Student> 所有学生列表
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                students.add(extractStudentFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("查询所有学生信息时发生错误: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * 根据专业查询学生信息
     * @param major 专业名称
     * @return List<Student> 该专业的学生列表
     */
    public List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, major);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(extractStudentFromResultSet(resultSet));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("查询学生信息时发生错误: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * 检查学号是否已存在
     * @param studentNo 学号
     * @return boolean 学号是否存在
     */
    public boolean isStudentNoExists(String studentNo) {
        String sql = "SELECT COUNT(*) FROM students WHERE student_no = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, studentNo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("检查学号是否存在时发生错误: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 从ResultSet提取学生对象（内部工具方法）
     * 
     * 功能:
     * 1. 将SQL结果集转换为Student对象
     * 2. 处理数据类型转换（如Date到LocalDate）
     * 
     * 注意:
     * - 被所有查询方法调用（减少重复代码）
     * - 需要处理可能的空值
     * 
     * @param resultSet 数据库查询结果集（必须包含学生表所有字段）
     * @return 填充好的Student对象
     * @throws SQLException 如果字段访问出错
     */
    private Student extractStudentFromResultSet(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        
        // 设置基本属性
        student.setId(resultSet.getInt("id"));
        student.setStudentNo(resultSet.getString("student_no"));
        student.setName(resultSet.getString("name"));
        student.setGender(resultSet.getString("gender"));
        
        // 转换SQL Date到Java 8 LocalDate
        java.sql.Date sqlDate = resultSet.getDate("birth_date");
        if (sqlDate != null) {
            student.setBirthDate(sqlDate.toLocalDate());
        }
        
        // 设置其他字段
        student.setMajor(resultSet.getString("major"));
        student.setClassName(resultSet.getString("class_name"));
        student.setPhone(resultSet.getString("phone"));
        student.setEmail(resultSet.getString("email"));
        student.setAddress(resultSet.getString("address"));
        
        return student;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return extractStudentFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("根据ID查询学生时出错: " + e.getMessage());
            return null;
        }
        return null;
    }
}
