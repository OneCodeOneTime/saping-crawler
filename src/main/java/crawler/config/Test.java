package crawler.config;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws ClientProtocolException, IOException{
		/*HttpClient hc = HttpClients.createMinimal();
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
		}*/
	}
}
