package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;
import com.backup.zgqsgw.Entity.InformatuonSchema;
import com.backup.zgqsgw.Vo.ObjectRestResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class JDBCUtil {


    /**
     * 返回查询失败或者成功
     * 成功则需要包含数据
     * @param dBentity
     */
    public static ObjectRestResponse getAllTableNamesByschemaname(DBentity dBentity) {


        String driver = getDriverType(dBentity.getDbType());
        String URL = getURL(dBentity);
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        String sql = "select * from information_schema.tables where table_schema='"+dBentity.getSchemaname()+"';";
        try
        {
            Class.forName(driver);
        }
        catch(java.lang.ClassNotFoundException e)
        {
            // System.out.println("Connect Successfull.");
            System.out.println("Cant't load Driver");
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
        try
        {
            List<InformatuonSchema> informatuonSchemaList = new ArrayList<>();
            con=DriverManager.getConnection(URL,dBentity.getUser(),dBentity.getPassword());
            st=con.createStatement();
            rs=st.executeQuery(sql);
            while(rs.next()){
                InformatuonSchema informatuonSchema=new InformatuonSchema();
                informatuonSchema.setTableSchema(rs.getString("TABLE_SCHEMA"));
                informatuonSchema.setTableName(rs.getString("TABLE_NAME"));
                informatuonSchema.setTableType(rs.getString("TABLE_TYPE"));
                informatuonSchema.setEngine(rs.getString("ENGINE"));
                informatuonSchema.setVersion(rs.getString("VERSION"));
                informatuonSchema.setRowFormat(rs.getString("ROW_FORMAT"));
                informatuonSchema.setTableRows(rs.getString("TABLE_ROWS"));
                informatuonSchema.setAvgRowLength(rs.getString("AVG_ROW_LENGTH"));
                informatuonSchema.setDataLenth(rs.getString("DATA_LENGTH"));
                informatuonSchema.setMaxDataLength(rs.getString("MAX_DATA_LENGTH"));
                informatuonSchema.setIndexLength(rs.getString("INDEX_LENGTH"));
                informatuonSchema.setDataFree(rs.getString("DATA_FREE"));
                informatuonSchema.setAutoIncrement(rs.getString("AUTO_INCREMENT"));
                informatuonSchema.setCreateTime(rs.getString("CREATE_TIME"));
                informatuonSchema.setUpdateTime(rs.getString("UPDATE_TIME"));
                informatuonSchemaList.add(informatuonSchema);
            }
            rs.close();
            st.close();
            con.close();
            return new ObjectRestResponse().data(informatuonSchemaList).rel(true);
        }
        catch(Exception e)
        {
            System.out.println("Connect fail:" + e.getMessage());
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
    }

    /**
     * 监控数据库信息
     * @param dBentity
     * @return
     */
    public static ObjectRestResponse getDBInfo(DBentity dBentity) {

        String driver = getDriverType(dBentity.getDbType());
        String URL = getURL(dBentity);
        Connection con = null;
        ResultSet rs = null;
        Statement st = null;
        String sql = "SELECT * FROM information_schema.GLOBAL_STATUS";
        try
        {
            Class.forName(driver);
        }
        catch(java.lang.ClassNotFoundException e)
        {

            System.out.println("Cant't load Driver");
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
       try
        {
            DBinfo dBinfo = new DBinfo();
            con=DriverManager.getConnection(URL,dBentity.getUser(),dBentity.getPassword());
            st=con.createStatement();
            rs=st.executeQuery(sql);
            Map<String,String> allinfo = new HashMap<>();

            while (rs.next()){

                allinfo.put(rs.getString("VARIABLE_NAME"),rs.getString("VARIABLE_VALUE"));
            }
            dBinfo = (DBinfo) PojoUtils.getPojo(dBinfo,allinfo);
            sql="SHOW VARIABLES LIKE 'max_connections'";
            rs=st.executeQuery(sql);

            if (rs.next()){
                dBinfo.setMAX_CONNECTIONS(rs.getString("Value"));
            }
            dBinfo.setDbip(dBentity.getIp());
            dBinfo.setSchemaname(dBentity.getSchemaname());
            rs.close();
            st.close();
            con.close();
            return new ObjectRestResponse().data(dBinfo).rel(true);
        }
        catch(Exception e)
        {
            System.out.println("Connect fail:" + e.getMessage());
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
    }





    public static String getURL(DBentity dBentity){
        switch (dBentity.getDbType()){
            case "mysql":
                return "jdbc:mysql://"+dBentity.getIp()+":3306/"+dBentity.getSchemaname();
            default:
                return  "jdbc:mysql://"+dBentity.getIp()+":3306/"+dBentity.getSchemaname();
        }
    }

    public static String getDriverType(String dbType){
         switch (dbType){
             case "mysql":
                 return "com.mysql.jdbc.Driver";

             default:
                 return  "com.mysql.jdbc.Driver";
         }
    }


    public  void ttt(String... abc){

        System.out.println(abc.length);
        System.out.println(abc[0]);
    }
    public static void main(String[] args) {
        new JDBCUtil().ttt("aa","aa","aaa");
    }

}
