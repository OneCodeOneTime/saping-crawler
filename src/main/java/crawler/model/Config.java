package crawler.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public static final String grabbedUrlFileName = "grabbedUrl.txt";

    /*ץȡ����url���·���������ļ���*/
    public static File grabbedUrlSave;

    static{
        imgSaveFiles = new ArrayList<File>();
        preparedURLs = new ArrayList<URL>();
    }
}
