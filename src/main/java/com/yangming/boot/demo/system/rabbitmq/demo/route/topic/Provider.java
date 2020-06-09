package com.yangming.boot.demo.system.rabbitmq.demo.route.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {

        Connection connection = RabbitMQUtils.getConnection();

        Channel channel = connection.createChannel();

        //参数1 交换机名称  参数2 direct 路由模式
        channel.exchangeDeclare("topics","topic");

        String routingKey = "user.get.findAll";

        channel.basicPublish("topics",routingKey,null,("这是direct routing key：["+routingKey+"]").getBytes());

        RabbitMQUtils.closeConnectionAndChannel(channel,connection);
    }
}
