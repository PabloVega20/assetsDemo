package interview.assets.demo.infraestructure.kafka.consumer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ReactiveKafkaConsumerTest {

  @Mock
  private KafkaReceiver<String, String> mockReceiver;

  @Mock
  private ReceiverRecord<String, String> mockRecord;

  @Mock
  private ReceiverOffset mockReceiverOffset;

  private ReactiveKafkaConsumer reactiveKafkaConsumer;

  @BeforeEach
  void setUp() {
    reactiveKafkaConsumer = new ReactiveKafkaConsumer(mockReceiver);
  }

  @Test
  @DisplayName("Should consume messages successfully")
  void testConsumeMessages() {
    String testMessage = "Test Kafka Message";

    when(mockReceiver.receive()).thenReturn(Flux.just(mockRecord));
    when(mockRecord.value()).thenReturn(testMessage);
    when(mockRecord.receiverOffset()).thenReturn(mockReceiverOffset);
    when(mockReceiverOffset.commit()).thenReturn(Flux.empty().then());

    StepVerifier.create(reactiveKafkaConsumer.consumeMessages())
        .expectNext(testMessage)
        .verifyComplete();

    verify(mockRecord).receiverOffset();
    verify(mockReceiverOffset).commit();
  }

  @Test
  @DisplayName("Should handle record processing error")
  void testProcessRecordError() {
    RuntimeException testException = new RuntimeException("Processing Error");

    when(mockReceiver.receive()).thenReturn(Flux.just(mockRecord));
    when(mockRecord.value()).thenThrow(testException);
    when(mockRecord.receiverOffset()).thenReturn(mockReceiverOffset);
    when(mockReceiverOffset.commit()).thenReturn(Flux.empty().then());

    StepVerifier.create(reactiveKafkaConsumer.consumeMessages())
        .expectError(RuntimeException.class)
        .verify();

    verify(mockRecord).receiverOffset();
    verify(mockReceiverOffset).commit();
  }

  @Test
  @DisplayName("Should process individual record successfully")
  void testProcessRecord() {
    String testMessage = "Test Kafka Message";

    when(mockRecord.value()).thenReturn(testMessage);
    when(mockRecord.receiverOffset()).thenReturn(mockReceiverOffset);
    when(mockReceiverOffset.commit()).thenReturn(Flux.empty().then());

    StepVerifier.create(reactiveKafkaConsumer.processRecord(mockRecord))
        .expectNext(testMessage)
        .verifyComplete();

    verify(mockRecord).receiverOffset();
    verify(mockReceiverOffset).commit();
  }

  @Test
  @DisplayName("Should handle processRecord method's error scenario")
  void testProcessRecordErrorHandling() {
    RuntimeException testException = new RuntimeException("Processing Error");

    when(mockRecord.value()).thenThrow(testException);
    when(mockRecord.receiverOffset()).thenReturn(mockReceiverOffset);
    when(mockReceiverOffset.commit()).thenReturn(Flux.empty().then());

    StepVerifier.create(reactiveKafkaConsumer.processRecord(mockRecord))
        .expectError(RuntimeException.class)
        .verify();

    verify(mockRecord).receiverOffset();
    verify(mockReceiverOffset).commit();
  }
}