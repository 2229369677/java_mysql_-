package com.student.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 数据库连接工具类
 * 负责管理MySQL数据库连接
 */
public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    // 数据库连接参数
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";

    // 静态块加载配置
    static {
        loadConfig();
    }

    /**
     * 从配置文件加载数据库配置
     */
    private static void loadConfig() {
        //Properties是Java标准库中用于处理​​键值对属性文件​​的工具类，继承自Hashtable，专门用于读取
        // 和写入.properties格式的配置文件。它的核心作用是将文件中的“键=值”对加载到内存中，并提供方法根据键获取对应的值。
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            //void load(InputStream inStream)：从输入流中读取属性列表
            // （键值对），并将其加载到Properties对象中。输入流的格式需符合.properties规范（每行一个key=value，支持注释#或!开头）。

            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");

            // 可选：从配置读取驱动
            if (prop.containsKey("db.driver")) {
                DRIVER = prop.getProperty("db.driver");
            }

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "错误：找不到配置文件 config.properties", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "加载配置文件时出错", e);
        }
    }

    /**
     * 获取数据库连接
     * @return Connection 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        // 检查配置是否加载成功
        if (URL == null || USERNAME == null || PASSWORD == null) {
            throw new SQLException("数据库配置未正确加载，请检查配置文件");
        }

        try {
            // 加载数据库驱动
            Class.forName(DRIVER);
            // 返回数据库连接
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "数据库驱动加载失败", e);
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
                logger.info("数据库连接已关闭");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "关闭数据库连接时发生错误", e);
            }
        }
    }

    /**
     * 测试数据库连接
     * @return boolean 连接是否成功
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            logger.info("数据库连接测试成功！");
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "数据库连接测试失败: URL=" + URL + ", USERNAME=" + USERNAME, e);
            return false;
        }
    }

    /**
     * 测试入口
     */
    public static void main(String[] args) {
        boolean success = testConnection();
        System.out.println("数据库连接测试: " + (success ? "成功" : "失败"));
        if (!success) {
            System.exit(1);
        }
    }
}