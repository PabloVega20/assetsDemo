package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adapter interface for retrieving and saving assets from a data source.
 * <p>
 * Defines methods for filtering assets and performing save operations using reactive programming
 * paradigms.
 */
public interface IGetAssetsByFilterAdapter {

  /**
   * Retrieves assets based on the provided filter criteria.
   *
   * @param uploadDateStart The start date for filtering by upload date range
   * @param uploadDateEnd   The end date for filtering by upload date range
   * @param filename        The filename expression for filtering (regex)
   * @param filetype        The file type for filtering
   * @return A Flux of Asset objects matching the criteria
   */
  Flux<Assets> getAssetsByFilter(
      LocalDateTime uploadDateStart,
      LocalDateTime uploadDateEnd,
      String filename,
      String filetype);

  /**
   * Saves an asset to the underlying data storage.
   *
   * @param assets The asset to be saved
   * @return A Mono signaling the completion of the save operation
   */
  Mono<Void> save(Assets assets);
}