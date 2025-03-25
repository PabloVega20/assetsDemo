package interview.assets.demo.config;

import interview.assets.demo.application.AssetsUploadRequestService;
import interview.assets.demo.application.GetAssetsByFilterService;
import interview.assets.demo.application.TaskSchedulerService;
import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestMapper;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.IKafkaProducer;
import interview.assets.demo.domain.interfaces.consumer.IKafkaConsumer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Value("${kafka.topic.assets.request}")
  private String assetsTopic;

  @Bean
  public AssetsUploadRequestService assetService(
      @Autowired IAssetsRequestAdapter assetsRequestAdapter,
      @Autowired IKafkaProducer kafkaProducer,
      @Autowired LoggerFactory loggerFactory,
      @Autowired IAssetsRequestMapper assetsRequestMapper) {
    return new AssetsUploadRequestService(assetsRequestAdapter, assetsTopic, kafkaProducer,
        loggerFactory, assetsRequestMapper);
  }

  @Bean
  public GetAssetsByFilterService getAssetsByFilterService(
      @Autowired IGetAssetsByFilterAdapter adapter,
      @Autowired LoggerFactory loggerFactory) {
    return new GetAssetsByFilterService(adapter, loggerFactory);
  }

  @Bean
  public TaskSchedulerService taskSchedulerService(@Autowired LoggerFactory log,
      @Autowired IKafkaConsumer kafkaConsumer, @Autowired IGetAssetsByFilterAdapter adapter,
      @Autowired IAssetsRequestAdapter mongoAdapter,
      @Autowired IAssetsRequestMapper assetsRequestMapper) {

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    return new TaskSchedulerService(log, scheduler, kafkaConsumer, adapter, mongoAdapter,
        assetsRequestMapper);
  }
}

