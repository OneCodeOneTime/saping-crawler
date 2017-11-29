package utils;


import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * �ļ���������
 * Created by baitp on 2017/11/28.
 */
public class FileUtils {
    /**
     *  ����·�����ļ�������Ŀ¼���ļ�
     */
    public static Path isNotExistCreate(String pathDir,String pathFile){
        if(StringUtils.isEmpty(pathDir) && StringUtils.isEmpty(pathFile)){
            return null;
        }
        //Ŀ¼·��Ϊ�գ��ļ�����Ϊ��
        if(StringUtils.isEmpty(pathDir)){
            return null;
        }
        //Ŀ¼·����Ϊ�գ��ļ���Ϊ��
        if(StringUtils.isEmpty(pathFile) && !StringUtils.isEmpty(pathDir)){
            Path path = Paths.get(pathDir);
            try {
                if(!path.toFile().exists()){
                    Files.createDirectories(path);
                }
                return path;
            } catch (IOException e) {
               return null;
            }
        }
        //Ŀ¼·�����ļ�������Ϊ��
        try {
            String pathAll = pathDir + File.separator + pathFile;
            Path path = Paths.get(pathAll);
            Files.createDirectories(Paths.get(pathDir));
            if(!path.toFile().exists()){
                Files.createFile(path);
            }

            return path;
        } catch (IOException e) {
           return null;
        }

    }
}
