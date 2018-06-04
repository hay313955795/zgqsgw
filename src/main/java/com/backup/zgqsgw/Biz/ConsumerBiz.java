package com.backup.zgqsgw.Biz;

import com.backup.zgqsgw.Entity.Consumer;
import com.backup.zgqsgw.Jpa.ConsumerJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hwb
 * @create 2018/5/31 8:20
 */
@Service
public class ConsumerBiz {


    @Autowired
    private ConsumerJpa consumerJpa;

    public void insertToTable(Consumer consumer){
        consumerJpa.save(consumer);
    }
}
