package cn.com.yeexun.meta_data.collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.yeexun.utils.CommonException;

public class JdbcHelper {
	
	private static Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
	
	public static Connection getConnection(String driver, String url, String username, String password) {
		Connection conn = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("加载数据库驱动时错误：" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			logger.error("获取数据库连接时错误：" + e);
			throw new CommonException("获取数据库连接失败");
		}
		return conn;
	}
	
	public static void close(Statement stmt, Connection conn) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			logger.error("关闭数据库连接时错误：" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			
			try {
				if (conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				logger.error("关闭数据库连接时错误：" + e.getMessage());
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	

}
