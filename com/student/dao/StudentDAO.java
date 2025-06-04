package com.student.dao;

import com.student.model.Student;
import com.student.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据访问对象类
 * 负责学生信息的数据库操作（增删改查）
 */
public class StudentDAO {
    
    /**
     * 添加学生信息
     * @param student 要添加的学生对象
     * @return boolean 添加是否成功
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_no, name, gender, birth_date, major, class_name, phone, email, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 设置参数
            statement.setString(1, student.getStudentNo());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender());
            statement.setDate(4, Date.valueOf(student.getBirthDate()));
            statement.setString(5, student.getMajor());
            statement.setString(6, student.getClassName());
            statement.setString(7, student.getPhone());
            statement.setString(8, student.getEmail());
            statement.setString(9, student.getAddress());
            
            // 执行插入操作
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("学生信息添加成功！");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("添加学生信息时发生错误: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 根据ID删除学生信息
     * @param id 学生ID
     * @return boolean 删除是否成功
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("学生信息删除成功！");
                return true;
            } else {
                System.out.println("未找到要删除的学生信息！");
            }
            
        } catch (SQLException e) {
            System.err.println("删除学生信息时发生错误: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 更新学生信息
     * @param student 要更新的学生对象
     * @return boolean 更新是否成功
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_no = ?, name = ?, gender = ?, birth_date = ?, major = ?, class_name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // 设置参数
            statement.setString(1, student.getStudentNo());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender());
            statement.setDate(4, Date.valueOf(student.getBirthDate()));
            statement.setString(5, student.getMajor());
            statement.setString(6, student.getClassName());
            statement.setString(7, student.getPhone());
            statement.setString(8, student.getEmail());
            statement.setString(9, student.getAddress());
            statement.setInt(10, student.getId());
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("学生信息更新成功！");
                return true;
            } else {
                System.out.println("未找到要更新的学生信息！");
            }
            
        } catch (SQLException e) {
            System.err.println("更新学生信息时发生错误: " + e.getMessage());
        }
        
        return false;
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
     * 从ResultSet中提取学生对象
     * @param resultSet 查询结果集
     * @return Student 学生对象
     * @throws SQLException SQL异常
     */
    private Student extractStudentFromResultSet(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setStudentNo(resultSet.getString("student_no"));
        student.setName(resultSet.getString("name"));
        student.setGender(resultSet.getString("gender"));
        
        // 处理日期转换
        Date birthDate = resultSet.getDate("birth_date");
        if (birthDate != null) {
            student.setBirthDate(birthDate.toLocalDate());
        }
        
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
