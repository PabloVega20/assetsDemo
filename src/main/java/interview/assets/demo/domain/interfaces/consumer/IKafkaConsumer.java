package interview.assets.demo.domain.interfaces.consumer;

import reactor.core.publisher.Flux;

/**
 * Interface for a reactive Kafka consumer. Defines methods for consuming messages from Kafka topics
 * in a reactive manner.
 */
public interface IKafkaConsumer {

  /**
   * Consumes messages from a Kafka topic and returns them as a reactive stream.
   *
   * @return A Flux that emits messages received from the Kafka topic
   */
  Flux<String> consumeMessages();
}