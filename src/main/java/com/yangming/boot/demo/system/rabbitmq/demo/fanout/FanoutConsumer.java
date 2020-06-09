package com.yangming.boot.demo.system.rabbitmq.demo.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutConsumer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,// 如如果不写名字  就是临时队列
                    exchange = @Exchange(value = "logs", type = "fanout")

            )
    })
    public void receive1(String msg) {
        System.out.println("consumer 1: " + msg);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,// 如如果不写名字  就是临时队列
                    exchange = @Exchange(value = "logs", type = "fanout")

            )
    })
    public void receive2(String msg) {
        System.out.println("consumer 2: " + msg);
    }
}
