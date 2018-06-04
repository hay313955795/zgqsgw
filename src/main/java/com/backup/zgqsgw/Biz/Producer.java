package com.backup.zgqsgw.Biz;

import com.backup.zgqsgw.Entity.DBentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author hwb
 * @create 2018/5/23 13:56
 */
@Service("producer")
public class Producer {

    /**
     * mq队列的生产者 会向特定的队列中插入数据
     */
    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    private JmsMessagingTemplate jmsTemplate;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage(Destination destination, final String message){

        jmsTemplate.convertAndSend(destination, message);
    }
}
