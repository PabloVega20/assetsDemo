package interview.assets.demo.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

/**
 * Configuration class for Reactive Kafka producer. Sets up the necessary beans and configuration
 * for reactive Kafka messaging.
 */
@Configuration
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
}