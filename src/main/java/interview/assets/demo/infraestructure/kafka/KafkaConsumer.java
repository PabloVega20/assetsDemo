package interview.assets.demo.infraestructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "test-topic", groupId = "my-group")
  public void listen(String message) {
    System.out.println("Mensaje recibido: " + message);
  }
}
