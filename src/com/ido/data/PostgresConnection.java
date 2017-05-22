package com.ido.data;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class PostgresConnection {
	Connection conn = null;
	final String url = System.getenv("JDBC_DATABASE_URL");
	static final Logger logger = Logger.getLogger(PostgresConnection.class);
	public Connection getConnection(){
		if(! "".equals(url)){
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
			}
			try{conn = DriverManager.getConnection(url);
			} catch (SQLException e) {
				logger.error(e.getErrorCode() + " " + e.getMessage());
			}
		}
		return conn;

	}
	public Connection getConnection(String URL){
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
			}
			conn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			logger.error(e.getErrorCode() + " " + e.getMessage());
		}
		return conn;
	}
	
	public void closeConnection(Connection conn){
		if( conn!= null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getErrorCode() + " " + e.getMessage());
			}
		}
	}
}
