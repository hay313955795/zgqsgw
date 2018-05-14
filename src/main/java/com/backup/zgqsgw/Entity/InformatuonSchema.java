package com.backup.zgqsgw.Entity;

import lombok.Data;

@Data
public class InformatuonSchema {

    private String tableSchema;
    private String tableName;
    private String tableType;
    private String engine;
    private String version;
    private String rowFormat;
    private String TableRows;
    private String avgRowLength;
    private String dataLenth;
    private String maxDataLength;
    private String indexLength;
    private String dataFree;
    private String autoIncrement;
    private String createTime;
    private String updateTime;

}
