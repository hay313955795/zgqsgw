package com.consumer.demo.biz;

import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

/**
 * @author hwb
 * @create 2018/5/23 13:57
 */
@Component
public class Consumer {

    @JmsListener(destination = "mytest.queue")
    public void receiveQueue(String text) throws InterruptedException {
        Gson gson =new Gson();
        DBentity dBentity = gson.fromJson(text,DBentity.class);
        System.out.println(dBentity);
        System.out.println("Consumer收到的报文为:"+text);

        sleep(10000);
    }
}
