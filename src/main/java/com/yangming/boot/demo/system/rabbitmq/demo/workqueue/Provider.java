package com.yangming.boot.demo.system.rabbitmq.demo.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yangming.boot.demo.system.util.RabbitMQUtils;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("work", true, false, false, null);

        for (int i = 0; i < 20; i++) {
            channel.basicPublish("", "work", null, (i + "hello work queue").getBytes());
        }

        RabbitMQUtils.closeConnectionAndChannel(channel, connection);
    }
}
