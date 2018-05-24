package com.backup.zgqsgw.Scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 * @create 2018/5/15 14:40
 */
@Component
public class Timingtask {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void ScheduledTest(){
        System.out.println("111");
    }
}
