package crawler.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public static final String grabbedUrlFileName = "grabbedUrl.txt";

    /*抓取过的url存放路径（包括文件）*/
    public static File grabbedUrlSave;

    static{
        imgSaveFiles = new ArrayList<File>();
        preparedURLs = new ArrayList<URL>();
    }
}
