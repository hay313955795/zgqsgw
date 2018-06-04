package com.backup.zgqsgw.Biz;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;
import com.backup.zgqsgw.Jpa.DBInfoJpa;
import com.backup.zgqsgw.Run.StartupRunner;
import com.backup.zgqsgw.Utils.PojoUtils;

import com.backup.zgqsgw.Vo.MonitorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hwb
 * @create 2018/5/15 15:55
 */
@Service
public class DBentityBiz {


    @Autowired
    private ServletContext application;
    @Resource
    private StartupRunner startupRunner;
    @Autowired
    private DBInfoJpa dbInfoJpa;

    /**
     * 刷新缓存
     * @param httpServletRequest
     * @throws Exception
     */
    public void refreshApplication(HttpServletRequest httpServletRequest) throws Exception {


        List<DBentity> dBentityList = (List<DBentity>)application.getAttribute("dBentityList");
        if(dBentityList.size()>0){
            DBentity dBentity = (DBentity)httpServletRequest.getSession().getAttribute("dbinfo");
            /**
             * 如果字段全部不为空则表示是从内部跳转从apllication中删除，并删除相应的数据
             */
            if(PojoUtils.isAllFieldNotNull(dBentity)){
                List<DBentity> dBentityList1 = new ArrayList<>();
                for(DBentity dBentity1:dBentityList){
                    if(dBentity1.equals(dBentity)){
                        continue;
                    }
                    dBentityList1.add(dBentity1);
                }
                startupRunner.refreshApplication(dBentityList1);
                //删除数据
                List<DBinfo> dBinfoList = getDbInfo(dBentity);
                dbInfoJpa.deleteAll(dBinfoList);
            }
        }
    }


    public List<MonitorVo>  getDBMonitor(DBentity dBentity){
        /**
         * 排序之后的集合需要 将他转换成需要的数据格式
         */
        List<DBinfo> dBinfoList =  getDbInfo(dBentity);
        List<MonitorVo> monitorVoList =new ArrayList<>();
        for(int index = 0;index<dBinfoList.size();index++){

            DBinfo  dBinfo = dBinfoList.get(index);

            Float QPS ,TPS, SelectCount , UpdateCount , InsertCount , DeleteCount;
            Long BytesSent, BytesReceived ;


            if(index == 0){
                 QPS =0.00f; //Float.valueOf(dBinfo.getQUESTIONS());
                 TPS =0.00f; //Float.valueOf(dBinfo.getCOM_COMMIT())+Float.valueOf(dBinfo.getCOM_ROLLBACK());
                 SelectCount =0.00f;// Float.valueOf(dBinfo.getCOM_SELECT());
                 UpdateCount =0.00f; //Float.valueOf(dBinfo.getCOM_UPDATE());
                 InsertCount =0.00f; //Float.valueOf(dBinfo.getCOM_INSERT());
                 DeleteCount =0.00f;// Float.valueOf(dBinfo.getCOM_DELETE());
                 BytesSent =0L; //Integer.valueOf(dBinfo.getBYTES_SENT());
                 BytesReceived =0L; //Integer.valueOf(dBinfo.getBYTES_RECEIVED());

            }else {
                 DBinfo  updBinfo = dBinfoList.get(index-1);
                 Long time = ((dBinfo.getCollectTime().getTime()-updBinfo.getCollectTime().getTime())/1000);

                 QPS = (Float.valueOf(dBinfo.getQUESTIONS())-Float.valueOf(updBinfo.getQUESTIONS()))/time.intValue();
                 TPS = ((Float.valueOf(dBinfo.getCOM_COMMIT())-Float.valueOf(updBinfo.getCOM_COMMIT()))+(Float.valueOf(dBinfo.getCOM_ROLLBACK())-Float.valueOf(updBinfo.getCOM_ROLLBACK())))/time.intValue();
                 SelectCount = (Float.valueOf(dBinfo.getCOM_SELECT())-Float.valueOf(updBinfo.getCOM_SELECT()))/time.intValue();
                 UpdateCount = (Float.valueOf(dBinfo.getCOM_UPDATE())-Float.valueOf(updBinfo.getCOM_UPDATE()))/time.intValue();
                 InsertCount = (Float.valueOf(dBinfo.getCOM_INSERT())-Float.valueOf(updBinfo.getCOM_INSERT()))/time.intValue();
                 DeleteCount = (Float.valueOf(dBinfo.getCOM_DELETE())-Float.valueOf(updBinfo.getCOM_DELETE()))/time.intValue();
                 BytesSent = Long.parseLong(dBinfo.getBYTES_SENT())-Long.parseLong(updBinfo.getBYTES_SENT());
                 BytesReceived = Long.parseLong(dBinfo.getBYTES_RECEIVED())-Long.parseLong(updBinfo.getBYTES_RECEIVED());
            }
            Integer ThreadsConnected = Integer.valueOf(dBinfo.getTHREADS_CONNECTED());
            Integer ThreadsRunning = Integer.valueOf(dBinfo.getTHREADS_RUNNING());
            Integer MaxConnections = Integer.valueOf(dBinfo.getMAX_CONNECTIONS());
            Integer SlowQueries = Integer.valueOf(dBinfo.getSLOW_QUERIES());

            MonitorVo monitorVo = new MonitorVo(QPS,TPS,SelectCount,UpdateCount,InsertCount,DeleteCount,ThreadsConnected,ThreadsRunning,MaxConnections,SlowQueries,BytesSent,BytesReceived);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            monitorVo.setCollectTime(simpleDateFormat.format(dBinfo.getCollectTime()));
            monitorVoList.add(monitorVo);
        }

        return monitorVoList;
    }



    public   List<DBinfo> getDbInfo(DBentity dBentity){

        DBinfo dBinfo = new DBinfo();
        dBinfo.setSchemaname(dBentity.getSchemaname());
        dBinfo.setDbip(dBentity.getIp());
        ExampleMatcher matcher = ExampleMatcher.matching()
                /**
                 * 姓名采用“开始匹配”的方式查询
                 */
                .withMatcher("dbip", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("schemaname",ExampleMatcher.GenericPropertyMatchers.contains());
                Example<DBinfo> ex = Example.of(dBinfo, matcher);
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<DBinfo> dBinfoList = dbInfoJpa.findAll(ex,sort);

        return dBinfoList;
    }


}
