package pl.eladutkiewicz.producent.producent;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ProducentApplication {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();


    public static void sendMessages() {

        executorService.execute(() -> {
            try {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61618");
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic("EXAMPLE_TOPIC");
                connection.start();
                int messageNumber = 0;
                while (true) {
                    Thread.sleep(2000);
                    String message = "Wiadomość nr: "+ messageNumber;
                    TextMessage textMessage = session.createTextMessage(message);
                    MessageProducer messageProducer = session.createProducer(topic);
                    messageProducer.send(textMessage);
                    messageNumber++;
                    System.out.println("Wiadomośc została wysłana " + message);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        });


    }

    public static void main(String[] args) {

        SpringApplication.run(ProducentApplication.class, args);
        sendMessages();
    }

}
