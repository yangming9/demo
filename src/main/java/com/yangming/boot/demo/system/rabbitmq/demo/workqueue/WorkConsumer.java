package com.yangming.boot.demo.system.rabbitmq.demo.workqueue;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer {

    @RabbitListener(queuesToDeclare = @Queue("work_springboot"))
    public void receive1(String msg) {
        System.out.println("msg1 = " + msg);
    }

    @RabbitListener(queuesToDeclare = @Queue("work_springboot"))
    public void receive2(String msg) {
        System.out.println("msg2 = " + msg);
    }
}
