package interview.assets.demo.infraestructure.kafka;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ReactiveKafkaProducerTest {

  @Mock
  private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaTemplate;

  private ReactiveKafkaProducer kafkaProducer;

  @BeforeEach
  void setUp() {
    kafkaProducer = new ReactiveKafkaProducer(reactiveKafkaTemplate);
  }

  @Test
  @DisplayName("Send message to Kafka topic successfully")
  void testSendMessageSuccess() {
    String topic = "test-topic";
    String message = "test-message";

    when(reactiveKafkaTemplate.send(eq(topic), isNull(), eq(message)))
        .thenReturn(Mono.empty());

    StepVerifier.create(kafkaProducer.sendMessage(topic, message))
        .verifyComplete();

    verify(reactiveKafkaTemplate).send(eq(topic), isNull(), eq(message));
  }

  @Test
  @DisplayName("Send message handles template send error")
  void testSendMessageError() {
    String topic = "test-topic";
    String message = "test-message";
    RuntimeException expectedException = new RuntimeException("Kafka send error");

    when(reactiveKafkaTemplate.send(eq(topic), isNull(), eq(message)))
        .thenReturn(Mono.error(expectedException));

    StepVerifier.create(kafkaProducer.sendMessage(topic, message))
        .verifyError(RuntimeException.class);
  }

  @Test
  @DisplayName("Send message with different topic and payload")
  void testSendMessageVariations() {
    String[] topics = {"topic1", "topic2", "another-topic"};
    String[] messages = {"payload1", "payload2", "different-payload"};

    for (String topic : topics) {
      for (String message : messages) {
        when(reactiveKafkaTemplate.send(eq(topic), isNull(), eq(message)))
            .thenReturn(Mono.empty());

        StepVerifier.create(kafkaProducer.sendMessage(topic, message))
            .verifyComplete();

        verify(reactiveKafkaTemplate).send(eq(topic), isNull(), eq(message));
      }
    }
  }
}