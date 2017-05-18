package com.ido.data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

public class BuildAccountTable {
		final static Logger logger = Logger.getLogger(BuildAccountTable.class);
		public BuildAccountTable(Connection conn, List<SobjectDescribe> fields) throws SQLException{
			final String mStatement = "DROP TABLE IF EXISTS sfaccount;"+
					"CREATE TABLE sfaccount();";
			
			Statement query = conn.createStatement();
			query.executeUpdate(mStatement);
			logger.info("Table sfaccount has been created");
			//now dynamically modify the table from SobjectDescribe info in batch this maps
			//PostgreSQL types to Salesforce data type using SOAPType from Salesforce SObject
			//getDescribe()
			String baseAlter = "ALTER TABLE sfaccount ADD COLUMN ";
			String statementSql = "";
			conn.setAutoCommit(false);
			Statement batch = conn.createStatement();
			for(SobjectDescribe s : fields){
				String mType = "";
				switch(s.getSoapType()){
				case "xsd:int":
					mType = s.getName() + " integer;";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:double":
					mType = s.getName()+ " numeric(" + s.getPrecision()+","+s.getScale()+");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:string":
					mType = s.getName() + " varchar("+ s.getLength() +");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:date":
					mType = s.getName() + " date;";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:dateTime":
					mType = s.getName() + " timestamp without time zone";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:bolean":
					mType = s.getName() + " boolean;";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:ID":
					mType = s.getName() + " varchar(" + s.getLength() +");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:anyType":
					mType = s.getName() + " varchar(" + s.getLength() +");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:base64Binary":
					mType = s.getName() + " varbit(" + s.getLength() +");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				}
			}
			int[] updateCounts = batch.executeBatch();
			logger.info(updateCounts + " fields were added to sfaccount table.");
			conn.commit();
		}
}
