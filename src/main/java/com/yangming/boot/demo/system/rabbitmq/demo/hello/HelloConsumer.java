package com.yangming.boot.demo.system.rabbitmq.demo.hello;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue("hello_world"))
public class HelloConsumer {

    @RabbitHandler
    public void recive1(String msg) {
        System.out.println("message = " + msg);
    }
}
