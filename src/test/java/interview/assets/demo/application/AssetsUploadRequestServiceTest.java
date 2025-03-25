package interview.assets.demo.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestMapper;
import interview.assets.demo.domain.interfaces.IKafkaProducer;
import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AssetsUploadRequestServiceTest {

  @Mock
  private IAssetsRequestAdapter assetsAdapter;

  @Mock
  private IKafkaProducer kafkaProducer;

  @Mock
  private LoggerFactory loggerFactory;

  @Mock
  private ILogger logger;

  @Mock
  private IAssetsRequestMapper assetsMapper;

  private AssetsUploadRequestService service;
  private static final String ASSETS_TOPIC = "test-assets-topic";

  @BeforeEach
  void setUp() {
    when(loggerFactory.getLogger(AssetsUploadRequestService.class)).thenReturn(logger);

    service = new AssetsUploadRequestService(
        assetsAdapter,
        ASSETS_TOPIC,
        kafkaProducer,
        loggerFactory,
        assetsMapper
    );
  }

  @Test
  @DisplayName("Successfully upload an asset and send Kafka message")
  void testSuccessfulAssetUpload() throws GeneralException {
    AssetsRequest assetsRequest = new AssetsRequest();
    assetsRequest.setFileName("test-file.txt");

    AssetsRequestDocument mockDocument = new AssetsRequestDocument();
    when(assetsMapper.toDocument(any(AssetsRequest.class))).thenReturn(mockDocument);
    when(assetsAdapter.upload(mockDocument)).thenReturn(Mono.just("test-asset-id"));
    when(kafkaProducer.sendMessage(ASSETS_TOPIC, "test-asset-id"))
        .thenReturn(Mono.empty());

    StepVerifier.create(service.uploadAsset(assetsRequest))
        .expectNext("test-asset-id")
        .verifyComplete();
  }

  @Test
  @DisplayName("Throw exception when file name is empty")
  void testEmptyFileName() {
    AssetsRequest assetsRequest = new AssetsRequest();
    assetsRequest.setFileName("");

    GeneralException exception = assertThrows(GeneralException.class, () -> {
      service.uploadAsset(assetsRequest).block();
    });

    assert exception.getMessage().contains("ERROR DURING THE ASSET REQUEST UPLOAD");
    assert exception.getCause() != null;
    assert exception.getCause().getMessage().equals("Assets file name cannot be empty");
  }

  @Test
  @DisplayName("Throw exception when file name is null")
  void testNullFileName() {
    AssetsRequest assetsRequest = new AssetsRequest();
    assetsRequest.setFileName(null);

    GeneralException exception = assertThrows(GeneralException.class, () -> {
      service.uploadAsset(assetsRequest).block();
    });

    assert exception.getMessage().contains("ERROR DURING THE ASSET REQUEST UPLOAD");
    assert exception.getCause() != null;
    assert exception.getCause().getMessage().equals("Assets file name cannot be empty");
  }
}