package com.student.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 负责管理MySQL数据库连接
 */
public class DatabaseConnection {
    // 数据库连接参数
    private static final String URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    
    // 数据库驱动
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * 获取数据库连接
     * @return Connection 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        try {
            // 加载数据库驱动
            Class.forName(DRIVER);
            // 返回数据库连接
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("数据库驱动加载失败: " + e.getMessage());
            throw new SQLException("数据库驱动加载失败", e);
        }
    }
    
    /**
     * 关闭数据库连接
     * @param connection 要关闭的连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 测试数据库连接
     * @return boolean 连接是否成功
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("数据库连接测试成功！");
            return true;
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            return false;
        }
    }
}
