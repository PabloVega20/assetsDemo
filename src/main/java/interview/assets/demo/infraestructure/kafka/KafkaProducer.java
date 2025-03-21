package interview.assets.demo.infraestructure.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
    sendMessage("Hello World");
  }

  public void sendMessage(String message) {
    kafkaTemplate.send("test-topic", message);
    System.out.println("Mensaje enviado: " + message);
  }
}
