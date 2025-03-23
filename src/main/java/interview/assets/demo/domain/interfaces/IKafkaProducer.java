package interview.assets.demo.domain.interfaces;

import reactor.core.publisher.Mono;

/**
 * Interface for Kafka producers that support reactive programming. Defines operations for sending
 * messages to Kafka topics in a non-blocking way.
 */
public interface IKafkaProducer {

  /**
   * Sends a message to a Kafka topic reactively.
   *
   * @param topic   The name of the Kafka topic
   * @param message The message to send
   * @return A Mono that completes when the message has been sent
   */
  Mono<Void> sendMessage(String topic, String message);
}