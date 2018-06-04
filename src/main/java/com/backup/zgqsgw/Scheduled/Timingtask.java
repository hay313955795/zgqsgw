package com.backup.zgqsgw.Scheduled;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;
import com.backup.zgqsgw.Jpa.DBInfoJpa;
import com.backup.zgqsgw.Utils.JDBCUtil;
import com.backup.zgqsgw.Vo.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.List;

/**
 * @author hwb
 * @create 2018/5/15 14:40
 */
@Component
public class Timingtask {

    @Autowired
    private ServletContext application;

    @Autowired
    private DBInfoJpa dbInfoJpa;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void ScheduledTest(){
        List<DBentity> dBentityList = (List<DBentity>)application.getAttribute("dBentityList");
        if(dBentityList.size()>0){
            for (DBentity dBentity :dBentityList){
                ObjectRestResponse objectRestResponse=  JDBCUtil.getDBInfo(dBentity);
                if (objectRestResponse.isRel()){
                    DBinfo dBinfo = (DBinfo)objectRestResponse.getData();
                    dBinfo.setCollectTime(new Date());
                    dbInfoJpa.save(dBinfo);
                    System.out.println("保存成功");
                }
            }
        }
        System.out.println("111");
    }
}
