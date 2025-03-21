package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

public interface IAssetGetAdapter {

  /**
   * Retrieves assets based on the provided filter criteria
   *
   * @param uploadDateStart The start date for filtering by upload date range
   * @param uploadDateEnd   The end date for filtering by upload date range
   * @param filename        The filename expression for filtering (regex)
   * @param filetype        The file type for filtering
   * @param sortDirection   The direction to sort results (ASC or DESC)
   * @return A Flux of Asset objects matching the criteria
   */
  Flux<Assets> getAsset(
      LocalDateTime uploadDateStart,
      LocalDateTime uploadDateEnd,
      String filename,
      String filetype,
      SortDirection sortDirection
  );

  /**
   * Enum representing sort direction options
   */
  enum SortDirection {
    ASC, DESC
  }
}
