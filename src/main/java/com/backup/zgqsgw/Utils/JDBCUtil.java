package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.InformatuonSchema;
import com.backup.zgqsgw.Vo.ObjectRestResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


    public static void main(String[] args) {
        long startTimes = System.currentTimeMillis();
        DBentity dBentity = new DBentity();
        dBentity.setDbType("mysql");
        dBentity.setIp("119.23.221.234");
        dBentity.setSchemaname("app");
        dBentity.setUser("three");
        dBentity.setPassword("jsd1406");
         ObjectRestResponse objectRestResponse= new JDBCUtil().getAllTableNamesByschemaname(dBentity);
         if(objectRestResponse.isRel()){
             List<InformatuonSchema> informatuonSchemaList =(List<InformatuonSchema>) objectRestResponse.getData();
             informatuonSchemaList.forEach(informatuonSchema ->System.out.println(informatuonSchema));
         }
         System.out.println(System.currentTimeMillis()-startTimes);
    }

}
