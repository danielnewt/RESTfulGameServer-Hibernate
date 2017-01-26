package game.tapticalwarfare.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Util {
	public static boolean IsEmptyString(String s) {
		if (s == null || s.trim().isEmpty())
			return true;
		return false;
	}

	public static String getRequestBody(HttpServletRequest r) throws IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = r.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		return data;
	}
	
	public static boolean getBoolValue(String value){
		if(value == null) return false;
		if(value.toLowerCase().equals("false")) return false;
		return true;
	}
}
