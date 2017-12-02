package crawler.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import crawler.crawStrategyImp.StrategyTest;
import crawler.crawlerService.CrawlerUtil;
import crawler.model.Config;
import org.springframework.web.servlet.FrameworkServlet;
import utils.FileUtils;
import utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;

public class ConfigInitializing extends FrameworkServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3494333391165568989L;

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("do dervice...");
	}
	
	/**
	 * 初始化参数
	 */
	@Override
	protected void initFrameworkServlet(){
		System.out.println("初始化参数...");
		URL url = getClass().getResource("../../config/config.json");
		JSONObject jo = JsonUtils.jsonFronJsonFile(url.getFile());

		//图像存储目录
		Object imgSave = jo.get("imgSave");
		if(null != imgSave && imgSave instanceof com.alibaba.fastjson.JSONArray){
			JSONArray imgSaveJa = (JSONArray)imgSave;
			int i = 0;
			Object imgSaveOj;
			while(i < imgSaveJa.size() && null != (imgSaveOj = imgSaveJa.get(i++)) && imgSaveOj instanceof  String){
				try{
					File file = new File(new String(imgSaveOj.toString().getBytes(),"UTF-8"));
					Path path = FileUtils.isNotExistCreate(file.getAbsolutePath(),"");
					if(null != path){
						Config.imgSaveFiles.add(path.toFile());
					}
				}catch(Exception e){
				}
			}
		}
		//种子URL
		Object preparedUrl = jo.get("preparedUrls");
		if(null != preparedUrl && preparedUrl instanceof com.alibaba.fastjson.JSONArray){
			JSONArray preparedUrls = (JSONArray)preparedUrl;
			int i = 0;
			Object urlOb;
			long successURL = 0;
			long failURL = 0;
			while(i<preparedUrls.size() && null != (urlOb = preparedUrls.get(i++)) && urlOb instanceof  String){
				try{
					URL purl = new URL(new String(urlOb.toString().getBytes(),"UTF-8"));
					Config.preparedURLs.add(purl);
					successURL++;
				}catch(Exception e){
					failURL++;
				}
			}
			System.out.println("种子URL录入成功："+successURL+";失败："+failURL);
		}

		//下载文件存储地址
		Object dlWebsPathOj = jo.get("downloadedWebpagePath");
		if(null != dlWebsPathOj && dlWebsPathOj instanceof String){
			try{
				String downloadedWebpagePathS = new String(dlWebsPathOj.toString().getBytes(),"UTF-8");
				Path path = FileUtils.isNotExistCreate(downloadedWebpagePathS,"");
				if(null != path) {
					Config.downloadedWebFile = path.toFile();
				}
			}catch(Exception e){
			}
		}

		//抓取过的url存放路径
		Object grabbedUrlDir = jo.get("grabbedUrlSaveDir");
		if(null != grabbedUrlDir && grabbedUrlDir instanceof String){
			try{
				String grabbedUrlDirString = new String(grabbedUrlDir.toString().getBytes(),"UTF-8");
				Path path = FileUtils.isNotExistCreate(grabbedUrlDirString,"");
				if(null != path) {
					Config.grabbedUrlSave = path.toFile();
				}
			}catch(Exception e){
			}
		}

		//检查参数配置是否合法
		if(Config.imgSaveFiles.size() == 0){
			throw new RuntimeException("图像存储路径为空！");
		}else{
			Iterator<File> itf = Config.imgSaveFiles.iterator();
			while(itf.hasNext()){
				File file = itf.next();
				if(!file.exists()){
					itf.remove();
				}
			}
			if(Config.imgSaveFiles.size() == 0){
				throw new RuntimeException("图像存储路径不合法！");
			}
		}

		if(Config.preparedURLs.size() == 0){
			throw new RuntimeException("无种子URL！");
		}
		if(null == Config.downloadedWebFile || !Config.downloadedWebFile.exists()){
			throw new RuntimeException("找不到下载网页存放路径！");
		}
		if(null == Config.grabbedUrlSave || !Config.grabbedUrlSave.exists()){
			throw new RuntimeException("找不到已抓取URL存放路径！");
		}

		//开始抓取种子url网页
		CrawlerUtil.craw(new StrategyTest(),Config.preparedURLs);
	}
	
}
