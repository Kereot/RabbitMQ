package com.flamexander.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class HomeworkConsumerApp {
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String queueName = channel.queueDeclare().getQueue();
            System.out.println("To subscribe enter 'subscribe', space and the desired topic name: 'php' or 'java'.");
            System.out.println("To unsubscribe enter 'unsubscribe', space and the subscribed topic name.");
            System.out.println("To exit enter 'exit'");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] command = line.split(" ");

                if (command[0].equals("exit")) {
                    scanner.close();
                    break;
                }

                try {
                    switch (command[0]) {
                        case "subscribe":
                            if (!command[1].equals("php") && !command[1].equals("java")) {
                                System.out.println("Unknown topic!");
                            } else {
                                channel.queueBind(queueName, EXCHANGE_NAME, command[1]);
                                System.out.println("Subscribed to topic: '" + command[1] + "'.");
                            }
                            break;
                        case "unsubscribe":
                            if (!command[1].equals("php") && !command[1].equals("java")) {
                                System.out.println("Unknown topic!");
                            } else {
                                channel.queueUnbind(queueName, EXCHANGE_NAME, command[1]);
                                System.out.println("Unsubscribed from topic: '" + command[1] + "'.");
                            }
                            break;
                        default:
                            System.out.println("Unknown command!");
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("No topic specified!");
                }
            }
        }
    }
}
