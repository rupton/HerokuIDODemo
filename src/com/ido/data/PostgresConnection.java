package com.ido.data;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class PostgresConnection {
	Connection conn = null;
	static final Logger logger = Logger.getLogger(PostgresConnection.class);
	public Connection getConnection(){
		String url = System.getenv("JDBC_DATABASE_URL");
		if(url == null || url.equals("")){
			logger.debug("Switching to alternate url");
			url ="jdbc:postgresql://ec2-34-206-239-11.compute-1.amazonaws.com:5432/dan5aser0k39ht?user=u81qb1t3r74suk"
						+ "&password=p4ab1144e9997175eff43ceada614c4bcd3487662e140c0cdccfbc928801d4516"
						+ "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		}
		logger.debug("Attempting database connection with: " + url);
		try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage());
			}
			try{conn = DriverManager.getConnection(url);
			} catch (SQLException e) {
				logger.error(e.getErrorCode() + " " + e.getMessage());
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

				logger.error(e.getErrorCode() + " " + e.getMessage());
			}
		}
	}
}
