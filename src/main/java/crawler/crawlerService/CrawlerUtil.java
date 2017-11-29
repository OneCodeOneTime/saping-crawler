package crawler.crawlerService;

import crawler.crawStrategy.CrawlerStrategy;

import java.net.URL;
import java.util.List;

/**
 * Created by dell on 2017/11/28.
 */
public class CrawlerUtil {
    private static CrawlerStrategy cs;

    /**
     * ×¥È¡ÍøÒ³
     */
    public static void craw(CrawlerStrategy csi,List<URL> urlList){
        CrawlerUtil.cs = csi;
        cs.crawler(urlList);
    }
}
