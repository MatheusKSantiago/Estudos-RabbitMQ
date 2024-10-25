package org.estudosRabbitMQ;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Trabalhador {
    public static String QUEUE_NAME = "filaDeTarefas";
    public static String HOST = "localhost";

    public static void main(String[] args) {
        var c1 = new Consumidor();
        var c2 = new Consumidor();

        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        t1.start();
        t2.start();
    }

    public static class Consumidor implements Runnable{
        @Override
        public void run() {
            var connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(HOST);

            try {
                var connection = connectionFactory.newConnection();
                var channel = connection.createChannel();
                channel.basicQos(1); //quantidade de mensagens que o consumidor pode receber sem precisar dar ACK por vez

                channel.queueDeclare(QUEUE_NAME,true,false,false,null);

                DeliverCallback deliverCallback = (tagConsumidor,delivery) -> {
                    String mensagem = new String(delivery.getBody(),"UTF-8");
                    try {
                        System.out.printf("[%s] está processando a tarefa: %s\n",tagConsumidor,mensagem);
                        fazerTrabalho(mensagem);
                        System.out.printf("[%s] Mensagem processada : %s\n",tagConsumidor,mensagem);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                };

                channel.basicConsume(QUEUE_NAME,deliverCallback, tag -> {});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        private void fazerTrabalho(String mensagem) throws InterruptedException {
            for(char ch : mensagem.toCharArray()){
                if (ch == '.')
                    Thread.sleep(1000);
            }
        }

    }


}

