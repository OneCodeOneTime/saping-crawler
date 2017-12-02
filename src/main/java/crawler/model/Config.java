package crawler.model;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 配置项
 * @author baitp
 *
 */
public class Config {
    /*图像存储的地方*/
	public static List<File> imgSaveFiles;
    /*种子URL*/
    public static List<URL> preparedURLs;
    /*已下载网页存储地址：一个目录*/
    public static File downloadedWebFile;

    /*抓取过的url存放文件名称*/
    /*public static final String grabbedUrlFileName = "grabbedUrl.txt";*/

    /*抓取过的url存放路径*/
    public static File grabbedUrlSave;

    /*已抓取url记录文件格式*/
    public static final String GRABBED_URL_FILE_FORMAT = "yyyy-MM-dd";

    /*不抓取的href形式*/
    public static final Set<String> NO_GRAB_HREFS =
            new HashSet<>(Arrays.asList("#","###","javascript:void(0)"));

    static{
        imgSaveFiles = new ArrayList<File>();
        preparedURLs = new ArrayList<URL>();
    }
}
