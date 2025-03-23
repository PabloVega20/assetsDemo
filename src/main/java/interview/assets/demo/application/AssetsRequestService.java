package interview.assets.demo.application;

import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestService;
import interview.assets.demo.domain.interfaces.IKafkaProducer;
import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import reactor.core.publisher.Mono;

/**
 * Service implementation for handling asset requests. Integrates with a data adapter and Kafka
 * messaging in a fully reactive manner.
 */
public class AssetsRequestService implements IAssetsRequestService {

  private final String assetsTopic;
  private final IAssetsRequestAdapter assetsAdapter;
  private final IKafkaProducer kafkaProducer;
  private final ILogger log;

  /**
   * Constructs a new AssetsRequestService with required dependencies.
   *
   * @param assetsAdapter The adapter for uploading asset requests
   * @param assetsTopic   The Kafka topic to which asset messages will be sent
   * @param kafkaProducer The reactive Kafka producer for sending messages
   */
  public AssetsRequestService(IAssetsRequestAdapter assetsAdapter, String assetsTopic,
      IKafkaProducer kafkaProducer,
      LoggerFactory log) {
    this.assetsAdapter = assetsAdapter;
    this.assetsTopic = assetsTopic;
    this.kafkaProducer = kafkaProducer;
    this.log = log.getLogger(AssetsRequestService.class);
  }

  /**
   * Uploads an asset and then publishes a message to Kafka with the asset ID. This method maintains
   * a fully reactive flow from start to finish.
   *
   * @param assets The asset request to upload
   * @return A Mono that emits the ID of the uploaded asset
   */
  @Override
  public Mono<String> uploadAsset(AssetsRequest assets) throws GeneralException {
    try {
      log.info(String.format("Uploading assetRequest: %s", assets));
      return assetsAdapter.upload(assets)
          .flatMap(id ->
              kafkaProducer.sendMessage(assetsTopic, id)
                  .thenReturn(id)
          );
    } catch (Exception e) {
      throw new GeneralException("ERROR DURING THE ASSET REQUEST UPLOAD ", e);
    }
  }
}