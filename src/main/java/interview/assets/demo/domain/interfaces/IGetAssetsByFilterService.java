package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

/**
 * Service interface for retrieving assets using dynamic filtering criteria.
 * <p>
 * Provides a method to fetch assets with complex filtering and sorting capabilities. Supports
 * filtering by date range, filename, file type, and sorting direction.
 */
public interface IGetAssetsByFilterService {

  /**
   * Retrieves assets based on specified filter and sorting criteria.
   *
   * @param uploadDateStart The start date for filtering upload date range
   * @param uploadDateEnd   The end date for filtering upload date range
   * @param filename        Filename filter criteria
   * @param filetype        File type filter criteria
   * @param sortDirection   Sorting direction (ascending or descending)
   * @return A Flux of Assets matching the specified criteria
   * @throws GeneralException If an error occurs during asset retrieval
   */
  Flux<Assets> getAssetsByFilter(
      LocalDateTime uploadDateStart,
      LocalDateTime uploadDateEnd,
      String filename,
      String filetype,
      String sortDirection
  ) throws GeneralException;
}
