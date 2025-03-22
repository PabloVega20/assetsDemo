package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

public interface IGetAssetsByFilterAdapter {

  /**
   * Retrieves assets based on the provided filter criteria
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
}
