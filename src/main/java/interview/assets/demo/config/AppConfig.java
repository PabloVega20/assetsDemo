package interview.assets.demo.config;

import interview.assets.demo.application.AssetsRequestService;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableReactiveMongoRepositories
public class AppConfig {

  @Bean
  public AssetsRequestService assetService(
      @Autowired IAssetsRequestAdapter assetsRequestAdapter) {
    return new AssetsRequestService(assetsRequestAdapter);
  }
}

