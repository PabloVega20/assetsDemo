package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import reactor.core.publisher.Mono;

public interface IAssetsRequestService {

  Mono<String> uploadAsset(AssetsRequest assets) throws GeneralException;
}
