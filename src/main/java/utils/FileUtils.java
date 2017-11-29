package utils;


import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件操作工具
 * Created by baitp on 2017/11/28.
 */
public class FileUtils {
    /**
     *  根据路径和文件名创建目录和文件
     */
    public static Path isNotExistCreate(String pathDir,String pathFile){
        if(StringUtils.isEmpty(pathDir) && StringUtils.isEmpty(pathFile)){
            return null;
        }
        //目录路径为空，文件名不为空
        if(StringUtils.isEmpty(pathDir)){
            return null;
        }
        //目录路径不为空，文件名为空
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
        //目录路径和文件名都不为空
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
