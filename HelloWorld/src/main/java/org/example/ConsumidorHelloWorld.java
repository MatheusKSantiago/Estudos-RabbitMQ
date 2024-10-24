package org.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.example.Constantes.HOST;
import static org.example.Constantes.QUEUE_NAME;

public class ConsumidorHelloWorld {

    public static void main(String[] args) throws IOException, TimeoutException {
        var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);

        Connection connection = connectionFactory.newConnection();
        var channel = connection.createChannel();
        //queueName | durable | exclusive | autoDelete | args
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        DeliverCallback deliverCallback = (consumerTag,message) -> {
          String mensagem = new String(message.getBody(),"UTF-8");
            System.out.printf("Consumidor %s recebeu: %s\n",consumerTag,mensagem);
        };
        //queueName | deliverCallback | cancelCallback
        channel.basicConsume(QUEUE_NAME,deliverCallback,consumerTag -> {
            System.out.printf("Consumidor %s foi cancelado\n",consumerTag);
            connection.close();
        });
    }
}
