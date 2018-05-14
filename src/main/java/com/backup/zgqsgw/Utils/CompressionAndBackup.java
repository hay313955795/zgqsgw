package com.backup.zgqsgw.Utils;


import com.backup.zgqsgw.Vo.ObjectRestResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件以及备份数据库
 * @author hwb
 * @create 2018/5/11 14:06
 */
public class CompressionAndBackup {

    public static ObjectRestResponse dozip(String srcfile, String zipfile) throws IOException {
        String temp = "";
        File src = new File(srcfile);
        File zipFile=new File(zipfile);
        //判断要压缩的文件存不存在
        if (!src.exists()) {
            System.err.println("要压缩的文件不存在！");
            System.exit(1);
            return new ObjectRestResponse("要压缩的文件不存在",false,1);
        }
        //如果说压缩路径不存/**/在，则创建
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        // 封装压缩的路径
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(zipfile));
        //这里可以加入校验
        //还可以设置压缩格式，默认UTF-8
        Charset charset = Charset.forName("GBK");
        ZipOutputStream zos = new ZipOutputStream(bos,charset);
        zip(src, zos, temp);
        //关闭流
        zos.flush();
        zos.close();
        System.out.println("压缩完成！");
        System.out.println("压缩文件的位置是：" + zipfile);
        return new ObjectRestResponse().rel(true).data("压缩完成");
    }

    private static void zip(File file, ZipOutputStream zos, String temp)
            throws IOException {
        // 如果不加"/"将会作为文件处理，空文件夹不需要读写操作
        if (file.isDirectory()) {
            String str = temp + file.getName() + "/";
            zos.putNextEntry(new ZipEntry(str));
            File[] files = file.listFiles();
            for (File file2 : files) {
                zip(file2, zos, str);
            }
        } else {
            ZipFile(file, zos, temp);
        }
    }

    private static void ZipFile(File srcfile, ZipOutputStream zos, String temp) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                srcfile));
        zos.putNextEntry(new ZipEntry(temp + srcfile.getName()));

        byte buf[] = new byte[1024];
        int len;
        while ((len = bis.read(buf)) != -1) {
            zos.write(buf, 0, len);
        }
        //按标准需要关闭当前条目，不写也行
        zos.closeEntry();
        bis.close();
    }


    public static void doSQL(String mysqldumpPath,String DBName,String tablename,String dbUser,String dbPassword,String dbIp,String toSavePath){
        try {
            System.out.println(mysqldumpPath+" "+DBName+" "+tablename+"  -u"+dbUser+" -p"+dbPassword+" -h"+dbIp+" --skip-extended-insert --default-character-set=utf8 --result-file="+toSavePath);
            Runtime.getRuntime()
                    .exec( mysqldumpPath+" "+DBName+" "+tablename+"  -u"+dbUser+" -p"+dbPassword+" -h"+dbIp+" --skip-extended-insert --default-character-set=utf8 --result-file="+toSavePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
