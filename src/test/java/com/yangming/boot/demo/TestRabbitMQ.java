package com.yangming.boot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRabbitMQ {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testFanout(){
        rabbitTemplate.convertAndSend("logs","","Fanout 模型消息");
    }

    @Test
    void testTopic(){
        rabbitTemplate.convertAndSend("topics_boot","user.get.findAll","发送 user.save key  路由消息");
    }

    @Test
    void testRouter(){
        rabbitTemplate.convertAndSend("directs","info","发送 info de key  路由消息");
    }

    @Test
    void testWork(){
        for (int i = 0;i<10;i++) {
            rabbitTemplate.convertAndSend("work_springboot", "work 模型"+i);
        }
    }

    @Test
    void testSendMessage(){
        rabbitTemplate.convertAndSend("hello_world","hello world!");
    }
}
