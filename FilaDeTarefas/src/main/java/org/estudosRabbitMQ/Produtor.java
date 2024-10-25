package org.estudosRabbitMQ;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Produtor {
    public static String QUEUE_NAME = "filaDeTarefas";
    public static String HOST = "localhost";

    public static void main(String[] args) throws IOException, TimeoutException {
        var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);

        var connection = connectionFactory.newConnection();
        var channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        System.out.println("Tudo ok! Pode escrever tarefas");
        while(true){
            var reader = new BufferedReader(new InputStreamReader(System.in));
            String mensagem = reader.readLine();
            channel.basicPublish("", QUEUE_NAME,MessageProperties.PERSISTENT_TEXT_PLAIN,mensagem.getBytes());
            System.out.printf("Mensagem enviada: %s\n",mensagem);
        }

    }
}
