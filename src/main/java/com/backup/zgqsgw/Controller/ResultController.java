package com.backup.zgqsgw.Controller;


import com.backup.zgqsgw.Biz.DBentityBiz;
import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Utils.CompressionAndBackup;
import com.backup.zgqsgw.Utils.FileUtilCustom;
import com.backup.zgqsgw.Utils.JDBCUtil;
import com.backup.zgqsgw.Vo.FileCreateVo;
import com.backup.zgqsgw.Vo.MonitorVo;
import com.backup.zgqsgw.Vo.ObjectRestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@RequestMapping("result")
@RestController
public class ResultController {


    @Value("${backupParam.mysqldumppath}")
    private String mysqldumppath;
    @Value("${backupParam.targetPath}")
    private String targetPath;


    @Autowired
    private ServletContext application;


    @Autowired
    private DBentityBiz dBentityBiz;
    /**
     * 提交备份
     * @return
     */
    @PostMapping("/backup")
    public ObjectRestResponse backup(String tablenames, HttpServletRequest httpServletRequest){

       if(StringUtils.isBlank(tablenames)) {
           return new  ObjectRestResponse("请选择需要备份的数据表",false,1);
       }
        //从session中读取对应的数据库信息
        DBentity dBentity = (DBentity)httpServletRequest.getSession().getAttribute("dbinfo");

        List<String> tablenamelist = Arrays.asList(tablenames.split(","));

        //在指定位置新建一个以时间开头的文件夹
        Date nowDate = new Date();
        System.out.println(nowDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(nowDate);
        FileCreateVo fileCreateVo =  new FileUtilCustom().createFile(targetPath+"\\"+dateNowStr);

        if (fileCreateVo.isSuccess()){
            for (String name :tablenamelist){
                 CompressionAndBackup.doSQL(mysqldumppath,dBentity.getSchemaname(),name,dBentity.getUser(),dBentity.getPassword(),dBentity.getIp(),fileCreateVo.getFilePath()+"\\"+name+".sql");
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        CompressionAndBackup.dozip(fileCreateVo.getFilePath(),fileCreateVo.getFilePath()+".zip");

                    }catch (Exception e){

                    }
                }
            }.start();

            /**
             * 可以将该压缩包以邮件的形式发送给对应的接收人
             */
            /**
             * 获取文件流
             */
            return new  ObjectRestResponse().rel(true).data("请到对应目录下"+fileCreateVo.getFilePath()+"查找备份文件");
        }else{
            return new  ObjectRestResponse("文件夹新建失败",false,1);
        }
    }






    /**
     * 查询数据库连接信息
     * @return
     */
    @GetMapping("/getinfo")
    public ObjectRestResponse getDBinfo(HttpServletRequest httpServletRequest){

        /**
         * 获取数据库配置信息
         */
        DBentity dBentity = (DBentity)httpServletRequest.getSession().getAttribute("dbinfo");
       /* DBentity dBentity = new DBentity();
        dBentity.setDbType("mysql");
        dBentity.setIp("119.23.221.234");
        dBentity.setPassword("jsd1406");
        dBentity.setSchemaname("app");
        dBentity.setUser("three");*/
        return JDBCUtil.getDBInfo(dBentity);

    }


    /**
     * 每隔两分钟请求一次数据
     */
    @GetMapping("/getmonitordata")
    public ObjectRestResponse getMonitorData(HttpServletRequest httpServletRequest){
        DBentity dBentity = (DBentity)httpServletRequest.getSession().getAttribute("dbinfo");
        String datalength = httpServletRequest.getParameter("datalength");
       /* System.out.println(datalength);
        DBentity dBentity = new DBentity();
        dBentity.setDbType("mysql");
        dBentity.setIp("119.23.221.234");
        dBentity.setPassword("jsd1406");
        dBentity.setSchemaname("app");
        dBentity.setUser("three");*/

        List<MonitorVo> monitorVoList = dBentityBiz.getDBMonitor(dBentity);

        return new ObjectRestResponse().rel(true).data( monitorVoList.subList(Integer.valueOf(datalength),monitorVoList.size()));
    }
}
