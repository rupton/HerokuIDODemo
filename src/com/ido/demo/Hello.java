package com.ido.demo;

import com.ido.data.PostgresConnection;
import com.ido.data.BuildAccountTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {
  // This method is called if TEXT_PLAIN is request
  /*@GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }
*/
  // This method is called if HTML is request
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String sayHtmlHello(@QueryParam("session") String session) throws SQLException, ClientProtocolException, IOException{
	  //build database
	  String uri = "jdbc:postgresql://ec2-34-206-239-11.compute-1.amazonaws.com:5432/dan5aser0k39ht?user=u81qb1t3r74suk"
				+ "&password=p4ab1144e9997175eff43ceada614c4bcd3487662e140c0cdccfbc928801d4516"
				+ "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
		Connection conn = new PostgresConnection().getConnection(uri);
		System.out.println("Salesforce session ID = " + session);
		String sfUrl = "https://nyccct-dev-ed.my.salesforce.com/services/data/v20.0/sobjects/Account/describe";
		HttpGet get = new HttpGet(sfUrl);
		get.setHeader("AUTHORIZATION", "Bearer " + session);
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(get);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while((line = reader.readLine())!=null){
			buffer.append(line);
		}
		JSONObject json = new JSONObject(buffer.toString());
		JSONArray fields = json.getJSONArray("fields");
		ArrayList<String> newFields = new ArrayList<String>();
		if(fields != null){
			
			for(int i = 0; i < fields.length(); i++){
				JSONObject field = fields.getJSONObject(i);
				String fieldAdd = field.getString("name");
				newFields.add(fieldAdd);
				System.out.println(field.getString("name"));				
			}
		}else{
			System.out.println("Couldn't get \"fields\" array");
		}
		if(newFields != null){
			new BuildAccountTable(conn, newFields);
			System.out.println("Database table \"sfaccount\" has been created");
		}
		
		return "{'message':'success'}";
  }

}