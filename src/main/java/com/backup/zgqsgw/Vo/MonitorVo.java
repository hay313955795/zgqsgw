package com.backup.zgqsgw.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hwb
 * @create 2018/5/17 12:44
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
@Data
public class MonitorVo  implements Serializable{


    private String  collectTime;
    private Float QPS;
    private Float TPS;
    private Float SelectCount;
    private Float UpdateCount;
    private Float InsertCount;
    private Float DeleteCount;
    private Integer ThreadsConnected;
    private Integer ThreadsRunning;
    private Integer MaxConnections;
    private Integer SlowQueries;
    private Long BytesSent;
    private Long BytesReceived;

    public MonitorVo(Float QPS, Float TPS, Float selectCount, Float updateCount, Float insertCount, Float deleteCount, Integer threadsConnected, Integer threadsRunning, Integer maxConnections, Integer slowQueries, Long bytesSent, Long bytesReceived) {
        this.QPS = QPS;
        this.TPS = TPS;
        SelectCount = selectCount;
        UpdateCount = updateCount;
        InsertCount = insertCount;
        DeleteCount = deleteCount;
        ThreadsConnected = threadsConnected;
        ThreadsRunning = threadsRunning;
        MaxConnections = maxConnections;
        SlowQueries = slowQueries;
        BytesSent = bytesSent;
        BytesReceived = bytesReceived;
    }
}
