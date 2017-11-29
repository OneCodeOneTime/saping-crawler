package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON操作工具
 * @author baitp
 *
 */
public class JsonUtils {
	/**
	 * 解析.json文件并返回json对象
	 * @param fileUrl
	 * @return JSONObject
	 * @throws IOException 
	 */
	public static JSONObject jsonFronJsonFile(String fileUrl){
		StringBuffer sb = new StringBuffer("");
		try(BufferedReader br  = new BufferedReader(new FileReader(fileUrl));){
			String line;
			while(null != (line = br.readLine())) {
				sb.append(line);
			}
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		return jsonFromString(sb.toString());
	}
	
	/**
	 * 解析json字符串
	 * @param jsonString
	 * @return JSONObject
	 */
	public static JSONObject jsonFromString(String jsonString) {
		Object jsonObject = JSONObject.parse(jsonString);
		if(null != jsonObject && jsonObject instanceof JSONObject) {
			return (JSONObject)jsonObject;
		}
		return null;
	}
}
