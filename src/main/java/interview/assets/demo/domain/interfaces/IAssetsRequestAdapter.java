package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.AssetsRequest;
import reactor.core.publisher.Mono;

public interface IAssetsRequestAdapter {

  Mono<String> upload(AssetsRequest assetsRequest);
}
