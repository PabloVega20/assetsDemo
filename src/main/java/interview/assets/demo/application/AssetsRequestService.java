package interview.assets.demo.application;

import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestService;
import interview.assets.demo.domain.objects.AssetsRequest;
import reactor.core.publisher.Mono;

public class AssetsRequestService implements IAssetsRequestService {

  private IAssetsRequestAdapter assetsAdapter;

  public AssetsRequestService(IAssetsRequestAdapter assetsAdapter) {
    this.assetsAdapter = assetsAdapter;
  }

  @Override
  public Mono<String> uploadAsset(AssetsRequest assets) {
    return assetsAdapter.upload(assets);
  }
}
