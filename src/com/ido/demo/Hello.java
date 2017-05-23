package com.ido.demo;

import com.ido.data.PostgresConnection;
import com.ido.data.BuildAccountTable;
import com.ido.data.BuildHelperFromJson;
import com.ido.data.SobjectDescribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.QueryParam;

// @formatter:off 
// @formatter:on

// Plain old Java Object it does not extend as class or implements
// an interface

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {

	Logger logger = Logger.getLogger("Hello.class");
  // This method is called if HTML is request
  // @session is either a session id or Authorization header passed in from Salesforce
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String sayHtmlHello(@QueryParam("session") String session) throws SQLException, ClientProtocolException, IOException, URISyntaxException{
	  
	  //build database
	  String uri = "jdbc:postgresql://ec2-34-206-239-11.compute-1.amazonaws.com:5432/dan5aser0k39ht?user=u81qb1t3r74suk"
				+ "&password=p4ab1144e9997175eff43ceada614c4bcd3487662e140c0cdccfbc928801d4516"
				+ "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	  	PostgresConnection pconn = new PostgresConnection();
		Connection conn = pconn.getConnection(uri);
logger.info("Salesforce session ID = " + session);
		String sfUrl = "https://nyccct-dev-ed.my.salesforce.com/services/data/v39.0/sobjects/Account/describe";
		HttpGet get = new HttpGet(sfUrl);
		get.setHeader("AUTHORIZATION", "Bearer " + session);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(get);
		
		//build request JSON posted to method
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while((line = reader.readLine())!=null){
			buffer.append(line);
		}
		JSONObject json = new JSONObject(buffer.toString());
		//end: build request JSON
		//build POJO helpers from request JSON
		BuildHelperFromJson helper = new BuildHelperFromJson();
		List<SobjectDescribe> results = helper.parseSObjectFromJSON(json);
		BuildAccountTable table = new BuildAccountTable(conn, results);
		logger.info("Dynamic sfaccount table has been created and is ready to receive archive data");
		pconn.closeConnection(conn);
		return "{'status':200, 'message':'success'}";
  }
  @POST
  @Path("{upload}")
  @Produces(MediaType.APPLICATION_JSON)
  public String uploadRecords(String requestBody) throws SQLException{
	  String uri = "jdbc:postgresql://ec2-34-206-239-11.compute-1.amazonaws.com:5432/dan5aser0k39ht?user=u81qb1t3r74suk"
				+ "&password=p4ab1144e9997175eff43ceada614c4bcd3487662e140c0cdccfbc928801d4516"
				+ "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	  	PostgresConnection pconn = new PostgresConnection();
		Connection conn = pconn.getConnection(uri);
		Statement statement = conn.createStatement();
	  logger.debug("Received upload request");
	  JSONArray json = new JSONArray(requestBody);
	  logger.debug("Length of input array = " + json.length());
	  for(int i = 0; i < json.length(); i++){
		  JSONObject record = json.getJSONObject(i);
		  Set<String> keys = record.keySet();
		  keys.remove("ShippingAddress");
		  keys.remove("BillingAddress");
		  for(String key: keys){
			  logger.debug(key + record.get(key));
		  }
	  }
	  return "{\"status\":200, \"message\":\"success\"}";
  }
  
  

}