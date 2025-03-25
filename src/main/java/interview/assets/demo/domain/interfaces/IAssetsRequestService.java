package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import reactor.core.publisher.Mono;

/**
 * Service interface for handling asset upload operations.
 * <p>
 * Defines the contract for uploading assets with reactive return type. Provides a method to process
 * and upload asset requests.
 */
public interface IAssetsRequestService {

  /**
   * Uploads an asset based on the provided asset request.
   *
   * @param assets The asset request to be uploaded
   * @return A Mono containing the ID of the uploaded asset
   * @throws GeneralException If an error occurs during the upload process
   */
  Mono<String> uploadAsset(AssetsRequest assets) throws GeneralException;
}