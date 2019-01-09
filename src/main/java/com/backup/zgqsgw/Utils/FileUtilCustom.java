package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Vo.FileCreateVo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

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

    public boolean deleteFile(String targetPath){
        File targetFile = new File(targetPath);
        if(targetFile.exists()){
            return targetFile.delete();
        }
        return false;
    }




    public static void writeToFile( String fileName,String str) {
        try {
            File f = new File(fileName);
            FileOutputStream fop = new FileOutputStream(f,true);
            // 构建FileOutputStream对象,文件不存在会自动新建
            OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
            writer.append(str);
            // 写入到缓冲区
            writer.append("\r\n");
            // 换行
            // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
            writer.close();
            // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
            fop.close();
        }catch (Exception e){

        }

    }


    public static void main(String[] args) {

        String dateNowStr = LocalDateTime.now().format(ofPattern("yyyy-MM-dd-HHmmss"));
        FileCreateVo file= new FileUtilCustom().createFile("E:\\MYSQL\\BACKUP\\DATE\\"+dateNowStr);
        System.out.println(file.getFilePath());
    }

}
