package com.backup.zgqsgw.Biz;

import com.backup.zgqsgw.Entity.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

/**
 * @author hwb
 * @create 2018/5/23 13:57
 */
@Component
public class Consumer5 {


    @Autowired
    private ConsumerBiz consumerBiz;
    /**
     * 模拟mq队列的消费者  接受来自于mytest.queue的消息
     * @param text
     * @throws InterruptedException
     */
    @JmsListener(destination = "mytest1.queue")
    public void receiveQueue(String text) throws InterruptedException {

        Consumer consumer = new Consumer();
        consumer.setFullmessage("我是消费者6Consumer收到的报文为:"+text);
        consumer.setMessage(text);
        consumerBiz.insertToTable(consumer);


        System.out.println("我是消费者6Consumer收到的报文为:"+text);
        sleep(1);

    }
}
