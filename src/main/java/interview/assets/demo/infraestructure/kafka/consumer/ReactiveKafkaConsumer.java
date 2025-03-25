package interview.assets.demo.infraestructure.kafka.consumer;

import interview.assets.demo.domain.interfaces.consumer.IKafkaConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

/**
 * Reactive Kafka consumer implementation using Reactor Kafka. Provides advanced message consumption
 * with explicit offset management.
 */
@AllArgsConstructor
@Component
@Slf4j
public class ReactiveKafkaConsumer implements IKafkaConsumer {

  private final KafkaReceiver<String, String> receiver;

  /**
   * Consumes messages from the Kafka topic with explicit offset management.
   *
   * @return A Flux that emits received messages and automatically commits offsets
   */
  @Override
  public Flux<String> consumeMessages() {
    return receiver.receive()
        .flatMap(this::processRecord);
  }

  /**
   * Processes individual Kafka records with explicit offset commitment.
   *
   * @param kafkaRecord The Kafka receiver record to process
   * @return Mono containing the processed message value
   */
  Mono<String> processRecord(ReceiverRecord<String, String> kafkaRecord) {
    return Mono.fromSupplier(kafkaRecord::value)
        .doOnSuccess(message -> {
          log.info("Received message: {}", message);
          kafkaRecord.receiverOffset().commit()
              .doOnSuccess(v -> log.info("Offset committed for message: {}", message))
              .doOnError(ex -> log.error("Failed to commit offset", ex))
              .subscribe();
        })
        .doOnError(ex -> {
          log.error("Error processing record", ex);
          kafkaRecord.receiverOffset().commit().subscribe();
        });
  }
}