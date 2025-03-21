package interview.assets.demo.persistence.adapters;

import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestMapper;
import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.persistence.documents.AssetsRequestDocument;
import interview.assets.demo.persistence.repositories.IAssetsRequestMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AssetsRequestAdapter implements IAssetsRequestAdapter {

  private final IAssetsRequestMongoRepository assetsRepository;
  private final IAssetsRequestMapper assetsMapper;

  @Override
  public Mono<String> upload(AssetsRequest assetsRequest) {
    AssetsRequestDocument assetsRequestDocument = assetsMapper.toDocument(assetsRequest);
    return assetsRepository.save(assetsRequestDocument).map(a -> a.getId());
  }
}
