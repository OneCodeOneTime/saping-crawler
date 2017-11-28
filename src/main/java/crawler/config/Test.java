package crawler.config;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException{
		HttpClient hc = HttpClients.createMinimal();
		HttpGet hg = new HttpGet("http://www.sina.com.cn/");
		
		HttpResponse response = hc.execute(hg);
		String content = EntityUtils.toString(response.getEntity(), "utf-8");
		//System.out.println(content);
		Document document = Jsoup.parse(content);
		Elements elements = document.getAllElements();
		for(int i = 0;i<elements.size();i++) {
			Element element = elements.get(i);
			System.out.println(element.attr("href"));
			//System.out.println(element);
		}
		
	}


}
