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

	  	PostgresConnection pconn = new PostgresConnection();
		Connection conn = pconn.getConnection();
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
		logger.debug("Verify sending " + results.size() + " fields");
		BuildAccountTable table = new BuildAccountTable();
		int[] buildResults = table.buildTable(conn, results);
		logger.info("Dynamic sfaccount table has been created and is ready to receive archive data");
		pconn.closeConnection(conn);
		return "{'status':200, 'message':'success'}";
  }
  @POST
  @Path("{upload}")
  @Produces(MediaType.APPLICATION_JSON)
  public String uploadRecords(String requestBody) throws SQLException, IOException{

	  	PostgresConnection pconn = new PostgresConnection();
		Connection conn = pconn.getConnection();
		logger.debug("Received upload request");
		JSONObject json = new JSONObject(requestBody);
		String session = json.getString("sessionid");
		JSONObject selector = json.getJSONObject("selector");
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
		JSONObject results = new JSONObject(buffer.toString());
		BuildHelperFromJson helper = new BuildHelperFromJson();
		List<SobjectDescribe> resultFields = helper.parseSObjectFromJSON(results);
		BuildAccountTable table = new BuildAccountTable();
	 int[] buildResults = table.insertValues(conn, resultFields, selector, session);
	 
	  return "{\"status\":200, \"message\":\"success\"}";
  }
  
  

}