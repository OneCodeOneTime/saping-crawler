package crawler.crawStrategyImp;

import crawler.crawStrategy.CrawlerStrategy;
import crawler.model.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dell on 2017/11/28.
 */
public class StrategyTest implements CrawlerStrategy{
    //��ҳ�����̳߳�
    protected static ExecutorService  execCraw = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //����ҳ���̳߳�
    protected static ExecutorService execSaveWeb = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //��ͼ���̳߳�
    protected static ExecutorService execSaveImg = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //���Ѿ�ץȡurl�̳߳�
    protected static ExecutorService execGrabbedUrl = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //������ҳ�������̵߳��̳߳�
    protected static ExecutorService execMakeCraw = Executors.newSingleThreadExecutor();
    //��������ҳ�̵߳��̳߳�
    protected static ExecutorService execMakeSaveWeb = Executors.newSingleThreadExecutor();
    //������ͼ����̵߳��̳߳�
    protected static ExecutorService execMakeSaveImg = Executors.newSingleThreadExecutor();
    //��������ץȡurl�̵߳��̳߳�
    protected static ExecutorService execMakeGrabbedURL = Executors.newSingleThreadExecutor();


    //�ȴ�ץȡ��url����
    protected static BlockingQueue<URL> waitingGrabQueue = new ArrayBlockingQueue<URL>(100,true);
    //�ȴ��洢����ץȡurl·������
    protected static BlockingQueue<String> graddedUrlQueue = new ArrayBlockingQueue<String>(1000,true);
    //�ȴ��洢��ͼ�����
    protected static BlockingQueue<URL> imgSaveQueue = new ArrayBlockingQueue<URL>(1000);
    //�ȴ��洢����ҳ����
    protected static BlockingQueue<Map<String,String>> webPagesToSave = new ArrayBlockingQueue<Map<String,String>>(1000);
    @Override
    public void crawler(List<URL> urlList){
        synchronized (waitingGrabQueue){
            waitingGrabQueue.addAll(urlList);
            System.out.println("urlList:"+urlList);
            System.out.println("��ѡURL��waitingGrabQueue:"+waitingGrabQueue);
            waitingGrabQueue.notifyAll();
        }
        try {
            Thread.sleep(1000L);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        //�����ĸ���������������߳�
        execMakeCraw.execute(new MakeCrawTask());
        execGrabbedUrl.execute(new MakeGrabbedURLTask());
        execMakeSaveWeb.execute(new MakeSaveWebTask());
    }
}

/**
 * ������ҳ�����̵߳��߳�:�Ӵ�ץȡurl������ȡ��url���������ύ��execCraw�̳߳�
 */
class MakeCrawTask implements Runnable{

    @Override
    public void run() {
        while(true){
            synchronized (StrategyTest.waitingGrabQueue){
                while(StrategyTest.waitingGrabQueue.isEmpty()){
                    StrategyTest.waitingGrabQueue.notifyAll();
                    try {
                        StrategyTest.waitingGrabQueue.wait();
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
                URL urlReadyToCraw = StrategyTest.waitingGrabQueue.poll();
                if(null != urlReadyToCraw){
                    System.out.println("��ץȡURL:"+urlReadyToCraw);
                    StrategyTest.execCraw.execute(new CrawOneUrl(urlReadyToCraw));
                    StrategyTest.waitingGrabQueue.notifyAll();
                }
            }
        }
    }
}
/**
 * ��������ҳ�̵߳��߳�
 */
class MakeSaveWebTask implements Runnable{

    @Override
    public void run() {
        while(true){
                synchronized (StrategyTest.webPagesToSave){
                    while(StrategyTest.webPagesToSave.isEmpty()){
                        StrategyTest.webPagesToSave.notifyAll();
                        try {
                            StrategyTest.webPagesToSave.wait();
                        }catch(Exception e){
                            Thread.currentThread().interrupt();
                        }
                    }
                    Collection<Map<String,String>> quickShot = new ArrayList<>();
                    StrategyTest.webPagesToSave.drainTo(quickShot);
                    StrategyTest.webPagesToSave.clear();
                    StrategyTest.execSaveWeb.execute(new SaveWebContentTpFile(Collections.unmodifiableCollection(quickShot)));
                    StrategyTest.webPagesToSave.notifyAll();
                }

        }
    }
}

/**
 * ������ͼ���̵߳��߳�
 */
class MakeMakeSaveImgTask implements Runnable{

    @Override
    public void run() {
        while(true){
            try {
                URL url = StrategyTest.imgSaveQueue.poll();
            }catch(Exception e){

            }
        }
    }
}

/**
 * �������Ѿ�ץȡurl���̣߳���graddedUrlQueue���Ѿ�ץȡurl���У�������ȡ���ݲ��������ύ��execGrabbedUrl�̳߳�
 */
class MakeGrabbedURLTask implements Runnable{

    @Override
    public void run() {
        while(true){
                synchronized (StrategyTest.graddedUrlQueue){
                    while(StrategyTest.graddedUrlQueue.isEmpty()){
                        StrategyTest.graddedUrlQueue.notifyAll();
                        try {
                            StrategyTest.graddedUrlQueue.wait();
                        }catch(InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                    }
                    //����һ������ȥ�洢������մ��洢����
                    Collection<String> c = new ArrayList<String>();
                    StrategyTest.graddedUrlQueue.drainTo(c);
                    StrategyTest.graddedUrlQueue.clear();
                    StrategyTest.execGrabbedUrl.execute(new SaveGrabbedUrlToFile(Collections.<String>unmodifiableCollection(c)));
                    StrategyTest.graddedUrlQueue.notifyAll();
                }
        }
    }
}


/**
 * ��ҳ�����߳�
 */
class CrawOneUrl implements Runnable{
    private URL url;
    public CrawOneUrl(URL url){
        this.url = url;
    }
    @Override
    public void run() {
        try {
            HttpClient hc = HttpClients.createMinimal();
            HttpGet hg = new HttpGet(url.toString());
            System.out.println("ready grab��"+url.toString());
            HttpResponse response = hc.execute(hg);
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            //System.out.println(content);
            Document document = Jsoup.parse(content);
            Elements elements = document.getAllElements();
            String title = "";
            boolean haveTitle = false;
            for(Element e:elements){
               if("title".equals(e.tagName())){
                   title = e.text();
                   haveTitle = true;
                   break;
               }
            }
            if(!haveTitle){
                title = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()+"@"+url.toString().hashCode());
            }

            //��url������ץȡ����
            synchronized (StrategyTest.graddedUrlQueue) {
                StrategyTest.graddedUrlQueue.add(url.toString());
                StrategyTest.graddedUrlQueue.notifyAll();
            }
            //����ҳ����
            synchronized (StrategyTest.webPagesToSave){
                Map<String,String> contentMap = new HashMap<>();
                contentMap.put("title",title);
                contentMap.put("content",content);
                StrategyTest.webPagesToSave.add(contentMap);
                StrategyTest.webPagesToSave.notifyAll();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("over grab��"+url.toString());
    }
}

//-------------------------------------------------------�ָ���---------------------------------------------------------
/**
 * �����ߣ�����ץȡurl
 */

class SaveGrabbedUrlToFile implements Runnable{
    //����д��
    private Collection<String> urls;
    public SaveGrabbedUrlToFile(Collection<String> urls){
        this.urls = urls;
    }
    @Override
    public void run() {
        //ʹ�ö���߳�������ץȡurl
        synchronized (Config.grabbedUrlSave){
            if(null != urls && urls.size() > 0){
                File file = Config.grabbedUrlSave;
                if(file.exists()){
                    StringBuffer sb = new StringBuffer("");
                    Iterator<String> it = urls.iterator();
                    while(it.hasNext()){
                        sb.append(it.next()+"  ");
                    }
                    try(FileChannel fc = new RandomAccessFile(Config.grabbedUrlSave,"rw").getChannel()){
                        //���ļ�β����ʼд
                        fc.position(fc.size());
                        ByteBuffer bb = ByteBuffer.allocate(sb.toString().getBytes().length);
                        bb.clear();
                        bb.put(sb.toString().getBytes());
                        //position�Ƶ���ͷ
                        bb.flip();
                        fc.write(bb);
                    }catch(IOException e){

                    }
                }else{
                    System.err.println("�����ץȡurl��¼��·����"+Config.grabbedUrlSave+"������");
                }
            }
        }
        //ʹ�õ��߳�������ץȡurl
       /* while(true){
            synchronized (graddedUrlQueue){
                while(graddedUrlQueue.isEmpty()){
                    try {
                        graddedUrlQueue.notifyAll();
                        graddedUrlQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                File file = Config.grabbedUrlSave;
                if(file.exists()){
                    String urls = graddedUrlQueue.poll();
                    System.out.println(urls);
                    try(FileChannel fc = new RandomAccessFile(Config.grabbedUrlSave,"rw").getChannel()){
                        //���ļ�β����ʼд
                        fc.position(fc.size());
                        saveNum++;
                        urls += "  ";

                        ByteBuffer bb = ByteBuffer.allocate(urls.getBytes().length);
                        bb.clear();
                        bb.put(urls.getBytes());
                        bb.flip();
                        fc.write(bb);
                    }catch(IOException e){
                        saveNum--;
                    }
                    graddedUrlQueue.notifyAll();
                }else{

                }

            }
        }*/
    }
}

/**
 * �����ߣ���ҳ���ݴ洢��һ����ҳ��Ӧһ���ļ�
 */
class SaveWebContentTpFile implements Runnable{
    /*
     *"title":"",
     *"content":""
     * */
    private Collection<Map<String,String>> contentCollection;
    public SaveWebContentTpFile(Collection<Map<String,String>> contentCollection){
        this.contentCollection = contentCollection;
    }
    @Override
    public void run() {
        synchronized (Config.downloadedWebFile){
            File dirSaveCotent = Config.downloadedWebFile;
            if(null != this.contentCollection && this.contentCollection.size() > 0){
                if(dirSaveCotent.exists()){
                    Iterator<Map<String,String>> it = this.contentCollection.iterator();
                    while(it.hasNext()){
                        Map<String, String> singleContent = it.next();
                        //ʹ��url������
                        Path path = FileUtils.isNotExistCreate(Config.downloadedWebFile.toPath().toString(),
                                singleContent.get("title")+".txt");
                        try (FileChannel fc = new RandomAccessFile(path.toFile(), "rw").getChannel()){
                            ByteBuffer bb = ByteBuffer.allocate(singleContent.get("content").toString().getBytes().length);
                            bb.clear();
                            bb.put(singleContent.get("content").toString().getBytes());
                            bb.flip();
                            fc.write(bb);
                        }catch(IOException ioe){
                            System.err.println(path.toFile().toString()+"����ʧ��");
                        }
                    }
                }else{
                    System.err.println("�����ץȡ��ҳ����Ŀ¼��·����"+Config.downloadedWebFile+"������");
                }
            }
        }
    }
}
