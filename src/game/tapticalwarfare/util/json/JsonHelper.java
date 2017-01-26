package game.tapticalwarfare.util.json;

import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

import game.tapticalwarfare.model.json.GameJson;

public class JsonHelper {

	
	public static String convertToJson(Serializable s){
		String json = null;
		try{
			ObjectMapper m = new ObjectMapper();
			json = m.writeValueAsString(s);
		} catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	public static Object convertToObject(String json){
		Object obj = null;
		try{
			ObjectMapper m = new ObjectMapper();
			obj = m.readValue(json, Object.class);
		} catch (Exception e){
			e.printStackTrace();
		}
		return obj;
	}
	
	public static GameJson convertToGameJson(String json){
		GameJson obj = null;
		try{
			ObjectMapper m = new ObjectMapper();
			obj = m.readValue(json, GameJson.class);
		} catch (Exception e){
			e.printStackTrace();
		}
		return obj;
	}
}
