package crawler.crawStrategy;

import java.net.URL;
import java.util.List;

/**
 * �����㷨����
 * Created by dell on 2017/11/28.
 */
public interface CrawlerStrategy {
        void crawler(List<URL> urlList);
}
