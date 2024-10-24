package org.example;

import static org.example.Constantes.*;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProdutorHelloWorld {

    public static String mensagem = "Hello world";

    public static void main(String[] args) throws IOException, TimeoutException {
        var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);

        try(Connection connection = connectionFactory.newConnection()){
            var channel = connection.createChannel();
            //queueName | durable | exclusive | autoDelete | args
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //exchange | routingKey | props | payload (byte[])
            channel.basicPublish("",QUEUE_NAME,null,mensagem.getBytes());
            System.out.println("Mensagem enviada!");
        }
    }
}
