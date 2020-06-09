package com.yangming.boot.demo.system.rabbitmq.demo.fanout;

import com.rabbitmq.client.*;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Consumer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        //绑定交换机
        channel.exchangeDeclare("logs","fanout");

        //临时队列
        String queue = channel.queueDeclare().getQueue();

        //绑定交换机和队列
        channel.queueBind(queue,"logs","");

        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override  //最后一个参数：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer 1： " + new String(body));

            }
        });
    }
}
