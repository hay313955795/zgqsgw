package com.backup.zgqsgw.Run;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;
import com.backup.zgqsgw.Utils.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hwb
 * @create 2018/5/15 14:19
 */
@Component(value = "startupRunner")
public class StartupRunner implements ApplicationRunner,ServletContextListener {

    @Autowired
    private ServletContext application =null ;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        application = servletContextEvent.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("启动加载数据");
        this.loadData();
    }

    public void loadData(DBentity dBentity) throws Exception {
        List<DBentity> dBentityList = (List<DBentity>)application.getAttribute("dBentityList");
        if(PojoUtils.isAllFieldNotNull(dBentityList) && PojoUtils.checkIsInList(dBentity,dBentityList)){
            dBentityList.add(dBentity);
            application.setAttribute("dBentityList",dBentityList);
        }
    }
    public void loadData(){
        List<DBentity> dBentityList = new ArrayList<>();
        application.setAttribute("dBentityList",dBentityList);
    }


    /**
     * 刷新application中的数据
     * @param dBentityList
     */
    public void refreshApplication(List<DBentity> dBentityList){
        application.setAttribute("dBentityList",dBentityList);
    }



}
