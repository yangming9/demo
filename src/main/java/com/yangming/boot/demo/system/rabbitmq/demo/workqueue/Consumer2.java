package com.yangming.boot.demo.system.rabbitmq.demo.workqueue;

import com.rabbitmq.client.*;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);//每次只消费一个消息
        channel.queueDeclare("work",true,false,false,null);
        // 参数2  不会自动确认  消息消费成功
        channel.basicConsume("work",false,new DefaultConsumer(channel){
            @Override  //最后一个参数：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)throws IOException{
                System.out.println("consumer 2： "+new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });

    }
}
