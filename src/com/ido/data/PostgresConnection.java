package com.ido.data;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgresConnection {
Connection conn = null;
	String uri = "jdbc:postgresql://ec2-34-206-239-11.compute-1.amazonaws.com:5432/dan5aser0k39ht?user=u81qb1t3r74suk"
		+ "&password=p4ab1144e9997175eff43ceada614c4bcd3487662e140c0cdccfbc928801d4516"
		+ "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	public Connection getConnection(){
		String myURL = System.getenv("JDBC_DATABASE_URL");
		if(myURL == null || "".equals(myURL)){
			return getConnection(uri);
		}else{
			return getConnection(myURL);
		}
	}
	public Connection getConnection(String URL){
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConnection(Connection conn) throws SQLException{
		if( conn!= null){
			conn.close();
		}
	}
}
