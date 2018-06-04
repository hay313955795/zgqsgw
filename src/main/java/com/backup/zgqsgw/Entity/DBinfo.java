package com.backup.zgqsgw.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hwb
 * @create 2018/5/15 12:50
 */

@Entity
@Table(name = "dbinfo")
@Data
public class DBinfo implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "dbip")
    private String dbip;

    @Column(name = "collect_time")
    private Date collectTime;

    @Column(name = "schemaname")
    private String schemaname;
    /**
     * QPS=（Questions2-Questions1）/Time
     */
    @Column(name = "QUESTIONS")
    private String QUESTIONS;

    /**
     * TPS=（（Com_commit2 - Com_commit1）+ （Com_rollback2 - Com_rollback1））/Time
     */
    @Column(name = "COM_COMMIT")
    private String COM_COMMIT;
    @Column(name = "COM_ROLLBACK")
    private String COM_ROLLBACK;

    /**
     * 增删改查的分量
     * select的量=（Com_select2 - Com_select1）/Time
     */
    @Column(name = "COM_SELECT")
    private String COM_SELECT;

    @Column(name = "COM_INSERT")
    private String COM_INSERT;

    @Column(name = "COM_UPDATE")
    private String COM_UPDATE;

    @Column(name = "COM_DELETE")
    private String COM_DELETE;


    /**
     * 连接数
     */
    @Column(name = "THREADS_CONNECTED")
    private String THREADS_CONNECTED;

    /**
     * 正在使用的连接数
     */
    @Column(name = "THREADS_RUNNING")
    private String THREADS_RUNNING;


    /**
     * SHOW VARIABLES LIKE 'max_connections'  最大连接数
     */
    @Column(name = "MAX_CONNECTIONS")
    private String MAX_CONNECTIONS;

    /**
     * 慢查询
     */
    @Column(name = "SLOW_QUERIES")
    private String SLOW_QUERIES;


    /**
     * 发送的流量=Bytes_sent2 - Bytes_sent2
     */
    @Column(name = "BYTES_SENT")
    private String BYTES_SENT;

    /**
     * 接收的流量=Bytes_received2 - Bytes_received1
     */
    @Column(name = "BYTES_RECEIVED")
    private String BYTES_RECEIVED;


    /**
     * 4.6 Innodb、MyISAM缓冲池
     一个数据库实例中可能有的表用Innodb存储引擎，有的表用MyISAM存储引擎，MySQL 默认的存储引擎 是InnoDB，它使用了一片称为缓冲池的内存区域，用于缓存数据表与索引的数据。缓冲池指标属于资源指标，而非工作指标，前者更多地用于调查（而非检测）性能问题。如果数据库性能开始下滑，而磁盘 I/O 在不断攀升，扩大缓冲池往往能带来性能回升。可以通过SHOW TABLE STATUS from database_name where Name='table_name';查询表的存储引擎是哪个。



     Innodb缓冲池使用率=(Innodb_buffer_pool_pages_total - Innodb_buffer_pool_pages_free)/Innodb_buffer_pool_pages_total，buffer_pool_pages是缓冲池的页数，默认每页为 16 KiB，或 16,384 字节，你可以使用：SHOW VARIABLES LIKE "innodb_page_size";查看，Innodb缓冲池的大小=Innodb_buffer_pool_pages_total*innodb_page_size。

     MyISAM缓冲池使用率=Key_blocks_used/(Key_blocks_unused +Key_blocks_used) 。默认MyISAM缓冲池的大小为16MB，可以使用SHOW VARIABLES LIKE '%key_buffer_size%';查看。
     */

    /**
     *  Innodb缓冲池使用率=(Innodb_buffer_pool_pages_total - Innodb_buffer_pool_pages_free)/Innodb_buffer_pool_pages_total
     */
    @Column(name = "INNODB_BUFFER_POOL_PAGES_TOTAL")
    private String INNODB_BUFFER_POOL_PAGES_TOTAL;
    @Column(name = "INNODB_BUFFER_POOL_PAGES_FREE")
    private String INNODB_BUFFER_POOL_PAGES_FREE;

    /**
     * MyISAM缓冲池使用率=Key_blocks_used/(Key_blocks_unused +Key_blocks_used)
     */
    @Column(name = "KEY_BLOCKS_USED")
    private String KEY_BLOCKS_USED;
    @Column(name = "KEY_BLOCKS_UNUSED")
    private String KEY_BLOCKS_UNUSED;

}
