package com.backup.zgqsgw.Controller;


import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Utils.JDBCUtil;
import com.backup.zgqsgw.Vo.ObjectRestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
public class IndexController {




    /**
     * 返回填写数据库信息的页面
     * @return
     */
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("result",new ObjectRestResponse().rel(true));
        return "index";
    }


    /**
     * 通过数据库信息获取所有表名称
     * @param dBentity
     * @param model
     * @return
     */
    @PostMapping("/loaddbinfo")
    public String LoadDBInfo(DBentity dBentity, Model model, HttpServletRequest httpServletRequest){

        ObjectRestResponse objectRestResponse = JDBCUtil.getAllTableNamesByschemaname(dBentity);
        if(objectRestResponse.isRel()){
            model.addAttribute("result",objectRestResponse);
            httpServletRequest.getSession().setAttribute("dbinfo",dBentity);
            return "main";
        }else {
            model.addAttribute("result",objectRestResponse);
            return "index";
        }

    }


}
