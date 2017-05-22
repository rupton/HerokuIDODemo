package com.ido.data;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.log4j.Logger;

public class ParseAccountJson {
	static final Logger logger = Logger.getLogger(ParseAccountJson.class);
	public void parseAccounts(JSONObject obj, String key){
		JSONArray mArray = obj.getJSONArray(key);
		parseAccounts(mArray);		
	}
	
	public void parseAccounts(JSONArray obj){
		for(int i = 0; i < obj.length(); i++){
			JSONObject mObj = obj.getJSONObject(i);
			stripFields(mObj);
		}
	}
	
	public void stripFields(JSONObject fieldList){
		for(String s: fieldList.keySet()){
			logger.debug((s + " " + fieldList.get(s).toString()));
		}
	}
}
