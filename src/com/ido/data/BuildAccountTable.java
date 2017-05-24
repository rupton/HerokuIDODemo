package com.ido.data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class BuildAccountTable {
		final static Logger logger = Logger.getLogger(BuildAccountTable.class);
		
		public int[] buildTable(Connection conn, List<SobjectDescribe> fields) throws SQLException{
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
				if(s.getType().equals("address")){
					continue;
				}
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
					mType = s.getName() + " timestamp";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "xsd:boolean":
					mType = s.getName() + " boolean;";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
					break;
				case "tns:ID":
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
				default:
					mType = s.getName() + " varchar(" + s.getLength() +");";
					statementSql = baseAlter + mType;
					batch.addBatch(statementSql);
				}
			}
			int[] updateCounts = batch.executeBatch();
			logger.info(updateCounts.length + " fields were added to sfaccount table.");
			conn.commit();
			return updateCounts;
		}
		public int[] insertValues(Connection conn, List<SobjectDescribe> fields, JSONObject selector, String session) throws ClientProtocolException, IOException, SQLException{
			//we'll need a list of types and their keys
			HashMap<String, String> fieldTypes = new HashMap<String, String>();
			//for(int i = 0 ; i < fields.size(); i++){
			StringBuffer buffer = new StringBuffer();
			String baseInsert = "INSERT INTO sfaccount ";
			String columns = "";
			String values = "";
			String insertValues = "";
			String baseSelect = "SELECT ";
			String postSelect = " FROM ACCOUNT WHERE " + selector.getString("where");
			for(SobjectDescribe s: fields){
				logger.debug(s.getName());
				buffer.append(s.getName()+",");
				fieldTypes.put(s.getName(), s.getSoapType());
			}
			
			values = buffer.toString().substring(0,buffer.length()-1);
			insertValues = values;
			logger.debug("Constructed values are " + values);
			String urlBase = "https://nyccct-dev-ed.my.salesforce.com/services/data/v39.0/query?q=";
			String jsonQueryString = baseSelect + values + postSelect;
			logger.debug(jsonQueryString);
			@SuppressWarnings("deprecation")
			String sfUrl = urlBase + URLEncoder.encode(jsonQueryString);
			HttpGet get = new HttpGet(sfUrl);
			get.setHeader("AUTHORIZATION", "Bearer " + session);
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(get);
			
			//build request JSON posted to method
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer querybuffer = new StringBuffer();
			String line = "";
			while((line = reader.readLine())!=null){
				querybuffer.append(line);
			}
			JSONObject results = new JSONObject(querybuffer.toString());
			JSONArray records = results.getJSONArray("records");
			conn.setAutoCommit(false);
			Statement batch = conn.createStatement();
			for(int i= 0; i< records.length(); i++){
				StringBuffer buffervals = new StringBuffer();
				StringBuffer buffercols = new StringBuffer();
				columns = "";
				values = "";
				JSONObject record = records.getJSONObject(i);
				Set<String> keys = record.keySet();
				keys.remove("BillingAddress");
				keys.remove("ShippingAddress");
				keys.remove("attributes");
				logger.debug("There are " + keys.size() + " keys");
				for(String s: keys){
					String fieldVal = fieldTypes.get(s);
					logger.debug("Matching " +s + "'s type which is " + fieldVal);
					if(record.get(s).toString().equalsIgnoreCase("null")){
						//insert literal null for SQL update
						values += record.get(s).toString();
						continue;
					}
					//align propper types
					switch(fieldVal){
						case "xsd:int":
							buffervals.append(record.getInt(s) + ",");
							logger.debug("Got int");
							break;
						case "xsd:double":
							buffervals.append(record.getDouble(s)+ ",");
							logger.debug("Got double");
							break;
						case "xsd:string":
							buffervals.append("'" +record.getString(s) + "',");
							logger.debug("Got String");
							break;
						case "xsd:date":
							buffervals.append("'" +record.getString(s) + "',");
							logger.debug("Got date");
							break;
						case "xsd:dateTime":
							buffervals.append("'" +record.getString(s) + "',");;
							logger.debug("Got datetim");
							break;
						case "xsd:boolean":
							buffervals.append((Boolean)record.get(s)+ ",");
							logger.debug("Got boolean");
							break;
						case "tns:ID":
							buffervals.append("'" +record.getString(s) + "',");
							logger.debug("Got id");
							break;
						case "xsd:anyType":
							buffervals.append(record.get(s));
							logger.debug("Got any");
							break;
						case "xsd:base64Binary":
							logger.debug("Got binary");
							buffervals.append(record.get(s));
							break;
						default:
							buffervals.append("'" +record.getString(s) + "',");
							break;
					}
					buffercols.append(s  + ",");
				}
				
				columns = buffercols.toString();
				columns = columns.substring(0, columns.length()-1);
				values = buffervals.toString();
				values = values.substring(0, values.length() -1);
				String statementSQL = baseInsert + "(" + columns +")" +" VALUES(" + values+");";
				batch.addBatch(statementSQL);
				logger.debug("Running query" + statementSQL);
			}
			int[] updateCounts = batch.executeBatch();
			conn.commit();
			return updateCounts;
		}
}
