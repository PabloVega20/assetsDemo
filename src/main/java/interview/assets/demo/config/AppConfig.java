package interview.assets.demo.config;

import interview.assets.demo.application.AssetsRequestService;
import interview.assets.demo.application.GetAssetsByFilterService;
import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class AppConfig {

  @Bean
  public AssetsRequestService assetService(
      @Autowired IAssetsRequestAdapter assetsRequestAdapter) {
    return new AssetsRequestService(assetsRequestAdapter);
  }

  @Bean
  public GetAssetsByFilterService getAssetsByFilterService(
      @Autowired IGetAssetsByFilterAdapter adapter) {
    return new GetAssetsByFilterService(adapter);
  }
}

