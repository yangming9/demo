package com.yangming.boot.demo.system.rabbitmq.demo.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        //将通道声明指定交换机
        //参数 1 交换机名称
        //参数 3 交换机类型 fanout 广播类型
        channel.exchangeDeclare("logs","fanout");

        //发送消息
        channel.basicPublish("logs","",null,"fanout type message".getBytes());

        RabbitMQUtils.closeConnectionAndChannel(channel,connection);
    }
}
