package crawler.config;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FrameworkServlet;

import com.alibaba.fastjson.JSONObject;

import utils.JsonUtils;

public class ConfigInitializing extends FrameworkServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3494333391165568989L;

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}
	
	/**
	 * 初始化参数
	 */
	@Override
	protected void initFrameworkServlet(){
		System.out.println("初始化参数...");
		URL url = getClass().getResource("../../config/config.json");
		JSONObject jo = JsonUtils.jsonFronJsonFile(url.getFile());
		
	}
	
}
