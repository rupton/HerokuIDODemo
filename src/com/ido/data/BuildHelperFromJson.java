package com.ido.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.log4j.Logger;

public class BuildHelperFromJson {
	final static Logger logger = Logger.getLogger(BuildHelperFromJson.class);

	public BuildHelperFromJson(){
		
	}
	
	public List<SobjectDescribe> parseSObjectFromJSON(JSONObject obj) {
		ArrayList<SobjectDescribe> sObjects = new ArrayList<SobjectDescribe>();
		JSONArray fields = obj.getJSONArray("fields");
		for(int i = 0; i < fields.length(); i++){
			JSONObject field = fields.getJSONObject(i);
			SobjectDescribe describe = new SobjectDescribe();
			describe.length = field.getInt("length");
			describe.name = field.getString("name");
			describe.nillable = field.getBoolean("nillable");
			describe.precision = field.getInt("precision");
			describe.scale = field.getInt("scale");
			describe.soapType = field.getString("soapType");
			describe.type = field.getString("type");
			sObjects.add(describe);
			logger.info("Adding " + describe.toString());
		}
		logger.debug(sObjects.size() + " SObjects have been queued to be added to the database");
		//SobjectDescribe describe = mapper.readValue(content, valueType);
		
		return sObjects;
	}
}
