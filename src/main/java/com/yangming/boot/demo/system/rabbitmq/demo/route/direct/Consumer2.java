package com.yangming.boot.demo.system.rabbitmq.demo.route.direct;

import com.rabbitmq.client.*;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        //通道声明交换机以及交换的类型
        channel.exchangeDeclare("logs.direct","direct");

        //创建临时队列
        String queueName = channel.queueDeclare().getQueue();

        //绑定基于 route key绑定队列和交换机

        channel.queueBind(queueName,"logs.direct","info");

        //消费消息
        channel.basicConsume(queueName,true,new DefaultConsumer(channel){
            @Override  //最后一个参数：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer 2： " + new String(body));

            }
        });
    }
}
