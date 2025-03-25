package interview.assets.demo.config;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

/**
 * Configuration class for Reactive Kafka producer. Sets up the necessary beans and configuration
 * for reactive Kafka messaging.
 */
@Configuration
@Slf4j
public class ReactiveKafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  /**
   * Creates and configures a ReactiveKafkaProducerTemplate for sending String messages. This bean
   * will be injected into the ReactiveKafkaProducer.
   *
   * @return A configured template for producing Kafka messages reactively
   */
  @Bean
  public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate() {

    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.RETRIES_CONFIG, 3);

    SenderOptions<String, String> senderOptions = SenderOptions.create(props);

    return new ReactiveKafkaProducerTemplate<>(senderOptions);
  }

  @Bean
  public KafkaReceiver<String, String> reactiveKafkaReceiver(
      @Value("${kafka.topic.assets.request}") final
      String assetsTopic, @Value("${kafka.group-id.assets}") final String groupId) {
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers);
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(
            consumerProps).commitInterval(Duration.ofSeconds(1))
        .commitBatchSize(10)
        .addAssignListener(partitions -> log.info("Assigned: {}", partitions))
        .addRevokeListener(partitions -> log.info("Revoked: {}", partitions))
        .subscription(Collections.singleton(assetsTopic));

    return KafkaReceiver.create(receiverOptions);
  }
}