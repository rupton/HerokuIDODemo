package com.ido.data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BuildAccountTable {

		public BuildAccountTable(Connection conn, ArrayList<String> fields) throws SQLException{
			final String mStatement = "DROP TABLE IF EXISTS sfaccount;"+
					"CREATE TABLE sfaccount();";
			
			Statement query = conn.createStatement();
			query.executeUpdate(mStatement);
			for(String s : fields){
				String mAlterStatement = "ALTER TABLE sfaccount ADD COLUMN " + s + " varchar(255)";
				Statement alterQuery = conn.createStatement();
				alterQuery.executeUpdate(mAlterStatement);
			}
		}
}
