package com.backup.zgqsgw.Controller;


import com.backup.zgqsgw.Biz.DBentityBiz;
import com.backup.zgqsgw.Biz.Producer;
import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Run.StartupRunner;
import com.backup.zgqsgw.Utils.JDBCUtil;
import com.backup.zgqsgw.Vo.ObjectRestResponse;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Set;


@Controller
@RequestMapping("")
public class IndexController {


   @Autowired
   private DBentityBiz dBentityBiz;



    @Resource
    private StartupRunner startupRunner;

    @Autowired
    private Producer producer;
    /**
     * 返回填写数据库信息的页面
     * @return
     */
    @GetMapping("/")
    public String index(Model model,HttpServletRequest httpServletRequest) throws Exception {

        /**
         * active mq
         *  调用生产者往队列中存入数据 */
             //Destination destination = new ActiveMQTempTopic("mytest1.queue");
           /* Destination destination = new ActiveMQQueue("mytest1.queue");
            for(int index=0;index<1000000;index++){
                producer.sendMessage(destination,"aaaa"+index);
            }
        */
        /**
         * redis
         */

        /*Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.ping());
        System.out.println(jedis.get("user"));
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }*/
        model.addAttribute("result",new ObjectRestResponse().rel(true));
        dBentityBiz.refreshApplication(httpServletRequest);
        return "index";
    }




    /**
     * 通过数据库信息获取所有表名称
     * @param dBentity
     * @param model
     * @return
     */
    @PostMapping("/loaddbinfo")
    public String LoadDBInfo(DBentity dBentity, Model model, HttpServletRequest httpServletRequest) throws Exception {



        ObjectRestResponse objectRestResponse = JDBCUtil.getAllTableNamesByschemaname(dBentity);
        if(objectRestResponse.isRel()){
            httpServletRequest.getSession().setAttribute("dbinfo",dBentity);
            /**
             * 将数据加载到缓存中
             */
            startupRunner.loadData(dBentity);
            return "main";
        }else {
            model.addAttribute("result",objectRestResponse);
            return "index";
        }

    }

    @GetMapping("/backup")
    public String  loadBackup(HttpServletRequest httpServletRequest, Model model){
        DBentity dBentity = (DBentity)httpServletRequest.getSession().getAttribute("dbinfo");
        ObjectRestResponse objectRestResponse = JDBCUtil.getAllTableNamesByschemaname(dBentity);
        model.addAttribute("result",objectRestResponse);
        return "backup";
    }

    @GetMapping("/link")
    public String loadMonitor(){
        return "link";
    }

}
