package com.flamexander.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class HomeworkProducerApp {
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            while (true) {
                channel.basicPublish(EXCHANGE_NAME, "php", null, messageBuilder("php").getBytes("UTF-8"));
                TimeUnit.SECONDS.sleep(10);
                channel.basicPublish(EXCHANGE_NAME, "java", null, messageBuilder("java").getBytes("UTF-8"));
                TimeUnit.SECONDS.sleep(10);
            }
        }
    }

    private static String messageBuilder(String language) {
        return language + " sample message, time: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}
