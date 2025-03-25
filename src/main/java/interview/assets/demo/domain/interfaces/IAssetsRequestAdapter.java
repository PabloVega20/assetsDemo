package interview.assets.demo.domain.interfaces;

import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import reactor.core.publisher.Mono;

/**
 * Adapter interface for managing asset request documents in a persistent storage.
 * <p>
 * Provides reactive methods for uploading and retrieving asset request documents. Serves as an
 * abstraction layer between the domain logic and the specific persistence mechanism (in this case,
 * MongoDB).
 * <p>
 * Key Responsibilities: - Upload asset request documents - Retrieve asset request documents by
 * their unique identifier
 * <p>
 * Utilizes Project Reactor's Mono for non-blocking, asynchronous operations.
 */
public interface IAssetsRequestAdapter {

  /**
   * Uploads an asset request document to the persistent storage.
   *
   * @param assetsRequest The asset request document to be uploaded
   * @return A Mono containing the unique identifier of the uploaded document
   */
  Mono<String> upload(AssetsRequestDocument assetsRequest);

  /**
   * Retrieves an asset request document by its unique identifier.
   *
   * @param id The unique identifier of the asset request document
   * @return A Mono containing the retrieved asset request document
   */
  Mono<AssetsRequestDocument> findById(String id);
}