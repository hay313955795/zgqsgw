package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Config.GlobalException;
import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;
import com.backup.zgqsgw.Entity.InformatuonSchema;
import com.backup.zgqsgw.Vo.ObjectRestResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import java.util.*;
import java.util.stream.StreamSupport;

import static com.backup.zgqsgw.Utils.FileUtilCustom.writeToFile;

public class JDBCUtil {


    private static Connection connection = null;

    public JDBCUtil(DBentity dBentity) throws GlobalException {
        String driver = getDriverType(dBentity.getDbType());
        String URL = getURL(dBentity);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new GlobalException("数据库链接异常，请检查");
        }
        try {
            connection = DriverManager.getConnection(URL, dBentity.getUser(), dBentity.getPassword());
        } catch (SQLException e) {
            throw new GlobalException("数据库链接异常，请检查");
        }
    }

    /**
     * 返回查询失败或者成功
     * 成功则需要包含数据
     *
     * @param dBentity
     */
    public static ObjectRestResponse getAllTableNamesByschemaname(DBentity dBentity) {


        ResultSet rs = null;
        Statement st = null;
        String sql = "select * from information_schema.tables where table_schema='" + dBentity.getSchemaname() + "';";

        try {
            List<InformatuonSchema> informatuonSchemaList = new ArrayList<>();

            st = connection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                InformatuonSchema informatuonSchema = new InformatuonSchema();
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
            return new ObjectRestResponse().data(informatuonSchemaList).rel(true);
        } catch (Exception e) {
            System.out.println("Connect fail:" + e.getMessage());
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
    }

    /**
     * 监控数据库信息
     *
     * @param dBentity
     * @return
     */
    public static ObjectRestResponse getDBInfo(DBentity dBentity) {
        ResultSet rs = null;
        Statement st = null;
        String sql = "SELECT * FROM information_schema.GLOBAL_STATUS";
        try {
            DBinfo dBinfo = new DBinfo();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            Map<String, String> allinfo = new HashMap<>();
            while (rs.next()) {
                allinfo.put(rs.getString("VARIABLE_NAME"), rs.getString("VARIABLE_VALUE"));
            }
            dBinfo = (DBinfo) PojoUtils.getPojo(dBinfo, allinfo);
            sql = "SHOW VARIABLES LIKE 'max_connections'";
            rs = st.executeQuery(sql);

            if (rs.next()) {
                dBinfo.setMAX_CONNECTIONS(rs.getString("Value"));
            }
            dBinfo.setDbip(dBentity.getIp());
            dBinfo.setSchemaname(dBentity.getSchemaname());
            rs.close();
            st.close();
            return new ObjectRestResponse().data(dBinfo).rel(true);
        } catch (Exception e) {
            return new ObjectRestResponse().message("数据库链接异常，请检查").rel(false);
        }
    }


    public static void doSQL(String DBName,String tablename,String toSavePath){
        ResultSet rs = null;
        Statement st = null;
        String createTableSql = String.format("show create table %s",tablename);

        String  columnAllColumnsSql = String.format("SELECT COLUMN_NAME columns  FROM information_schema.COLUMNS "+
                " WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'",DBName,tablename);

        String selectSql= "select %s from %s";


        try {
            st = connection.createStatement();
            rs = st.executeQuery(createTableSql);
            if (rs.next()){
                String createSql = rs.getString(2);

                writeToFile(toSavePath+"\\"+tablename+".sql",createSql+";");
                //追加写入文件
            }
            rs = st.executeQuery(columnAllColumnsSql);

            List<String> columnAllColumns = new ArrayList<>();

            while (rs.next()){
                columnAllColumns.add(rs.getString("columns")) ;
            }

            selectSql = String.format(selectSql,String.join(",",columnAllColumns),tablename);
            rs = st.executeQuery(selectSql);
            while (rs.next()){
                joinToInsert(rs,columnAllColumns,String.join(",",columnAllColumns),tablename,toSavePath);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    //
    public static void joinToInsert(ResultSet rs,List<String> columns,String columnAllColumns,String tableName,String toSavePath) throws SQLException {
        String value ="";

        for (String column:columns){
            value += "'"+rs.getString(column)+"',";
        }
        //最后一个字符串是， 必须要去除
        value = value.substring(0,value.length() - 1);
        String insertSql = String.format("insert into %s (%s) values(%s);", tableName, columnAllColumns,
                value);

        writeToFile(toSavePath+"\\"+tableName+".sql",insertSql);
       //写入文件
    }

    public static String getURL(DBentity dBentity) {
        switch (dBentity.getDbType()) {
            case "mysql":
                return "jdbc:mysql://" + dBentity.getIp() + ":3306/" + dBentity.getSchemaname()+"?zeroDateTimeBehavior=convertToNull";
            default:
                return "jdbc:mysql://" + dBentity.getIp() + ":3306/" + dBentity.getSchemaname()+"?zeroDateTimeBehavior=convertToNull";
        }
    }
    public static String getDriverType(String dbType) {
        switch (dbType) {
            case "mysql":
                return "com.mysql.jdbc.Driver";

            default:
                return "com.mysql.jdbc.Driver";
        }
    }


}
