package com.yangming.boot.demo.system.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {

    public static Connection getConnection() {
        try {
            //创建连接mq的连接工厂对象
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("192.168.3.188");
            connectionFactory.setPort(5672);
            connectionFactory.setVirtualHost("/ems");
            connectionFactory.setUsername("yangming");
            connectionFactory.setPassword("yangming");

            Connection connection = connectionFactory.newConnection();

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConnectionAndChannel(Channel channel,Connection connection){

        try {
            if (channel != null){
                channel.close();
            }
            if (connection != null){
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
