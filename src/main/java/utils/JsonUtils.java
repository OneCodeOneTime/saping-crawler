package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

/**
 * JSON��������
 * @author baitp
 *
 */
public class JsonUtils {
	/**
	 * ����.json�ļ�������json����
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
	 * ����json�ַ���
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
