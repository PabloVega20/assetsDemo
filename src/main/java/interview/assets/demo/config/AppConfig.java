package interview.assets.demo.config;

import interview.assets.demo.application.AssetsRequestService;
import interview.assets.demo.application.GetAssetsByFilterService;
import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.IKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class AppConfig {

  @Value("${kafka.topic.assets.request}")
  private String assetsTopic;

  @Bean
  public AssetsRequestService assetService(
      @Autowired IAssetsRequestAdapter assetsRequestAdapter,
      @Autowired IKafkaProducer kafkaProducer,
      @Autowired LoggerFactory loggerFactory) {
    return new AssetsRequestService(assetsRequestAdapter, assetsTopic, kafkaProducer,
        loggerFactory);
  }

  @Bean
  public GetAssetsByFilterService getAssetsByFilterService(
      @Autowired IGetAssetsByFilterAdapter adapter,
      @Autowired LoggerFactory loggerFactory) {
    return new GetAssetsByFilterService(adapter, loggerFactory);
  }
}

