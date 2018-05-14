package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Vo.FileCreateVo;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hwb
 * @create 2018/5/11 14:40
 */
public class FileUtilCustom {

    /**
     * 新建文件夹用以存放数据
     * 返回一个文件路径
     * @param targetPath
     * @return
     */
    public FileCreateVo createFile(String targetPath){

        File targetFile = new File(targetPath);

        boolean fileIsExists = targetFile.exists();

        if(fileIsExists){
           return createFile(targetPath,1);
        }

        boolean fileIsMkdirs =  targetFile.mkdirs();
        if(!fileIsMkdirs){
            return new FileCreateVo(targetPath,false);
        }

        return new FileCreateVo(targetPath,true);
    }

    public FileCreateVo createFile(String targetPath,Integer times){

        File targetFile = new File(targetPath+"("+times+")");

        boolean fileIsExists = targetFile.exists();

        if(fileIsExists){
            times++;
            return createFile(targetPath,times);
        }

        boolean fileIsMkdirs =  targetFile.mkdirs();

        if(!fileIsMkdirs){
            return new FileCreateVo(targetPath+"("+times+")",false);
        }

        return new FileCreateVo(targetPath+"("+times+")",true);
    }

    public static void main(String[] args) {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        FileCreateVo file= new FileUtilCustom().createFile("E:\\MYSQL\\BACKUP\\DATE\\"+dateNowStr);
        System.out.println(file.getFilePath());
    }

}
