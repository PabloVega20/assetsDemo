package interview.assets.demo.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestMapper;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.domain.interfaces.consumer.IKafkaConsumer;
import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.enums.AssetStatus;
import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TaskSchedulerServiceTest {

  @Mock
  private LoggerFactory loggerFactory;

  @Mock
  private ILogger logger;

  @Mock
  private ScheduledExecutorService scheduler;

  @Mock
  private IKafkaConsumer kafkaConsumer;

  @Mock
  private IGetAssetsByFilterAdapter adapter;

  @Mock
  private IAssetsRequestAdapter mongoAdapter;

  @Mock
  private IAssetsRequestMapper assetsRequestMapper;

  @Mock
  private AssetsRequestDocument mockDocument;

  @Mock
  private Assets mockAsset;

  private TaskSchedulerService taskSchedulerService;

  @BeforeEach
  void setUp() {
    when(loggerFactory.getLogger(AssetsUploadRequestService.class)).thenReturn(logger);
    taskSchedulerService = new TaskSchedulerService(
        loggerFactory,
        scheduler,
        kafkaConsumer,
        adapter,
        mongoAdapter,
        assetsRequestMapper
    );
  }

  @Test
  @DisplayName("Start method schedules task at fixed rate")
  void testStart() {
    taskSchedulerService.start();

    verify(scheduler, times(2)).scheduleAtFixedRate(
        any(Runnable.class),
        eq(0L),
        eq(10L),
        eq(TimeUnit.SECONDS)
    );
  }

  @Test
  @DisplayName("Process document successfully maps and saves asset")
  void testProcessDocumentSuccess() {
    when(kafkaConsumer.consumeMessages()).thenReturn(Flux.just("testDocId"));
    when(mongoAdapter.findById("testDocId")).thenReturn(Mono.just(mockDocument));
    when(assetsRequestMapper.assetsRequestToAssets(mockDocument)).thenReturn(mockAsset);
    when(adapter.save(mockAsset)).thenReturn(Mono.empty());
    when(mongoAdapter.upload(any(AssetsRequestDocument.class))).thenReturn(Mono.empty());

    taskSchedulerService.executeTask();

    verify(logger, times(5)).info(any(String.class));
    verify(adapter).save(mockAsset);
    verify(mongoAdapter).upload(any(AssetsRequestDocument.class));
  }

  @Test
  @DisplayName("Process document handles errors gracefully")
  void testProcessDocumentError() {
    when(kafkaConsumer.consumeMessages()).thenReturn(Flux.just("testDocId"));
    when(mongoAdapter.findById("testDocId")).thenReturn(
        Mono.error(new RuntimeException("Test error")));

    taskSchedulerService.executeTask();

    verify(logger).error(any(String.class), any(Throwable.class));
  }

  @Test
  @DisplayName("Update document status changes to PROCESSED")
  void testUpdateDocumentStatus() {
    when(mockDocument.getId()).thenReturn("testDocId");
    when(mongoAdapter.upload(mockDocument)).thenReturn(Mono.empty());

    StepVerifier.create(taskSchedulerService.updateDocumentStatus(mockDocument))
        .expectComplete()
        .verify();

    verify(mockDocument).setStatus(AssetStatus.PROCESSED);
    verify(mongoAdapter).upload(mockDocument);
    verify(logger).info(any(String.class));
  }
}