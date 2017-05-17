package com.ido.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateTable {

	public void createContactTable(String dburi) throws SQLException{
		String statement="DROP TABLE IF EXISTS CONTACT2; CREATE TABLE CONTACT2"
				+ "("
				+ "		ID SERIAL PRIMARY KEY NOT NULL,"
				+ "		firstname varchar(255) NOT NULL,"
				+ "		lastname varchar(255) NOT NULL,"
				+ "		account integer,"
				+ "		created_date date,"
				+ "		FOREIGN KEY(account) references account(id)"
				+ ")";
		String statement2 ="DROP SCHEMA IF EXISTS forceetl; CREATE SCHEMA forceetl";
		Connection dbConnection = new PostgresConnection().getConnection(dburi);
		Statement mStatement = dbConnection.createStatement();
		mStatement.executeUpdate(statement);
		mStatement.executeUpdate(statement2);
		buildTable(dbConnection);
		mStatement.close();
		dbConnection.close();
		
	}
	public void insertSampleContact(){
		
	}
	
	public void buildTable(Connection conn) throws SQLException{
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("name");
		fields.add("address");
		
		String fieldUpdateStatement = null;
		
		for(String s: fields){
			System.out.println("Adding " + s + " as a field to the table");
			fieldUpdateStatement = "alter table contact2 add column " + s + "  varchar(255) ";
			Statement mStatement = conn.createStatement();
			mStatement.executeUpdate(fieldUpdateStatement);
		}
		
	}
}
