package interview.assets.demo.persistence.mongo.adapters;

import interview.assets.demo.domain.interfaces.IAssetsRequestAdapter;
import interview.assets.demo.domain.interfaces.IAssetsRequestMapper;
import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import interview.assets.demo.persistence.mongo.repositories.IAssetsRequestMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Adapter implementation for managing asset request documents in MongoDB.
 * <p>
 * This class provides a bridge between the domain logic and the MongoDB repository, handling the
 * persistence and retrieval of asset request documents.
 * <p>
 * It implements the {@link IAssetsRequestAdapter} interface to provide reactive database operations
 * for asset requests.
 *
 * @see IAssetsRequestMongoRepository
 * @see IAssetsRequestMapper
 */
@Component
@AllArgsConstructor
public class AssetsRequestAdapter implements IAssetsRequestAdapter {

  /**
   * MongoDB repository for asset request documents.
   */
  private final IAssetsRequestMongoRepository assetsRepository;

  /**
   * Uploads an asset request document to the MongoDB repository.
   *
   * @param assetsRequest The asset request document to be saved
   * @return A {@link Mono} containing the ID of the saved document
   */
  @Override
  public Mono<String> upload(AssetsRequestDocument assetsRequest) {
    return assetsRepository.save(assetsRequest).map(AssetsRequestDocument::getId);
  }

  /**
   * Retrieves an asset request document by its unique identifier.
   *
   * @param id The unique identifier of the asset request document
   * @return A {@link Mono} containing the retrieved asset request document
   */
  @Override
  public Mono<AssetsRequestDocument> findById(String id) {
    return assetsRepository.findById(id);
  }
}