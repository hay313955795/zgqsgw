package com.backup.zgqsgw.Controller;


import com.backup.zgqsgw.Biz.DBentityBiz;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Run.StartupRunner;
import com.backup.zgqsgw.Utils.JDBCUtil;
import com.backup.zgqsgw.Vo.ObjectRestResponse;


import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
public class IndexController {


   @Autowired
   private DBentityBiz dBentityBiz;



    @Resource
    private StartupRunner startupRunner;


    /**
     * 返回填写数据库信息的页面
     * @return
     */
    @GetMapping("/")
    public String index(Model model,HttpServletRequest httpServletRequest) throws Exception {


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

        //获得连接之后 不关闭连接
        new JDBCUtil(dBentity);

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
