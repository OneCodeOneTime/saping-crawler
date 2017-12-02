package crawler.model;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * ������
 * @author baitp
 *
 */
public class Config {
    /*ͼ��洢�ĵط�*/
	public static List<File> imgSaveFiles;
    /*����URL*/
    public static List<URL> preparedURLs;
    /*��������ҳ�洢��ַ��һ��Ŀ¼*/
    public static File downloadedWebFile;

    /*ץȡ����url����ļ�����*/
    /*public static final String grabbedUrlFileName = "grabbedUrl.txt";*/

    /*ץȡ����url���·��*/
    public static File grabbedUrlSave;

    /*��ץȡurl��¼�ļ���ʽ*/
    public static final String GRABBED_URL_FILE_FORMAT = "yyyy-MM-dd";

    /*��ץȡ��href��ʽ*/
    public static final Set<String> NO_GRAB_HREFS =
            new HashSet<>(Arrays.asList("#","###","javascript:void(0)"));

    static{
        imgSaveFiles = new ArrayList<File>();
        preparedURLs = new ArrayList<URL>();
    }
}
