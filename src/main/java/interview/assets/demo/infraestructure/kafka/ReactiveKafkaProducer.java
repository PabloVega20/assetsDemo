package interview.assets.demo.infraestructure.kafka;

import interview.assets.demo.domain.interfaces.IKafkaProducer;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Reactive implementation of the IKafkaProducer interface. This class provides a non-blocking way
 * to send messages to Kafka topics using Project Reactor's reactive types.
 */
@Component
@AllArgsConstructor
public class ReactiveKafkaProducer implements IKafkaProducer {

  private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaTemplate;

  /**
   * Sends a message to a specified Kafka topic in a reactive way.
   *
   * @param topic   The Kafka topic to send the message to
   * @param message The message content to be sent
   * @return A Mono that completes when the message has been sent successfully
   */
  @Override
  public Mono<Void> sendMessage(String topic, String message) {
    return reactiveKafkaTemplate.send(topic, null, message)
        .then();
  }
}