package interview.assets.demo.application;

import static java.time.LocalTime.now;

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
import reactor.core.publisher.Mono;

/**
 * Service responsible for scheduling and processing asset upload tasks from Kafka. Periodically
 * consumes messages, processes documents, and updates their status.
 */
public class TaskSchedulerService {

  private final ScheduledExecutorService scheduler;
  private final ILogger log;
  private final IKafkaConsumer kafkaConsumer;
  private final IGetAssetsByFilterAdapter adapter;
  private final IAssetsRequestAdapter mongoAdapter;
  private final IAssetsRequestMapper assetsRequestMapper;

  /**
   * Constructs a TaskSchedulerService with necessary dependencies. Automatically starts the
   * scheduling process upon instantiation.
   *
   * @param log                 Logger factory for creating application logs
   * @param scheduler           Executor service for scheduling periodic tasks
   * @param kafkaConsumer       Kafka message consumer
   * @param adapter             PostgreSQL adapter for saving assets
   * @param mongoAdapter        MongoDB adapter for document operations
   * @param assetsRequestMapper Mapper for converting document types
   */
  public TaskSchedulerService(
      LoggerFactory log,
      ScheduledExecutorService scheduler,
      IKafkaConsumer kafkaConsumer,
      IGetAssetsByFilterAdapter adapter,
      IAssetsRequestAdapter mongoAdapter,
      IAssetsRequestMapper assetsRequestMapper) {
    this.log = log.getLogger(AssetsUploadRequestService.class);
    this.scheduler = scheduler;
    this.kafkaConsumer = kafkaConsumer;
    this.adapter = adapter;
    this.mongoAdapter = mongoAdapter;
    this.assetsRequestMapper = assetsRequestMapper;
    start();
  }

  /**
   * Starts the scheduled task to run every minute. Uses a fixed-rate scheduling to consume and
   * process Kafka messages.
   */
  public void start() {
    scheduler.scheduleAtFixedRate(this::executeTask, 0, 10, TimeUnit.SECONDS);
  }

  /**
   * Executes the primary task of consuming and processing Kafka messages. Handles document
   * retrieval, mapping, and status updates across multiple data stores.
   */
  void executeTask() {
    log.info("Initiating Kafka consumer task at: " + now());

    kafkaConsumer.consumeMessages()
        .flatMap(this::processDocument)
        .onErrorContinue((ex, obj) ->
            log.error("Global error processing Kafka message: " + obj, ex))
        .subscribe(
            v -> log.info("Completed Kafka message processing"),
            error -> log.error("Unhandled error in message processing flux", error));
  }

  /**
   * Processes an individual document from Kafka. Retrieves document from MongoDB, maps to Assets,
   * saves to PostgreSQL, and updates document status.
   *
   * @param documentId Unique identifier of the document to process
   * @return Mono representing the completion of document processing
   */
  private Mono<AssetsRequestDocument> processDocument(String documentId) {
    return mongoAdapter.findById(documentId)
        .flatMap(document -> {
          log.info(
              "Processing document: " + document.getFileName() + " with ID: " + document.getId()
                  + " and type: " + document.getContentType());

          Assets asset = assetsRequestMapper.assetsRequestToAssets(document);
          log.info("Mapped asset details: " + asset);

          return Mono.when(
                  adapter.save(asset),
                  updateDocumentStatus(document)
              )
              .thenReturn(document);
        })
        .doOnError(ex -> log.error("Failed to process document ID: " + documentId, ex))
        .onErrorResume(ex -> Mono.empty());
  }

  /**
   * Updates the status of a document to PROCESSED in MongoDB.
   *
   * @param document Document to update
   * @return Mono representing the completion of status update
   */
  Mono<Void> updateDocumentStatus(AssetsRequestDocument document) {
    document.setStatus(AssetStatus.PROCESSED);
    return mongoAdapter.upload(document)
        .then(Mono.fromRunnable(() ->
            log.info("Updated document " + document.getId() + " status to PROCESSED")
        ));
  }
}