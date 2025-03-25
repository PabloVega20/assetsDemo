package interview.assets.demo.application;

import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterService;
import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import java.time.LocalDateTime;
import java.util.Comparator;
import reactor.core.publisher.Flux;

/**
 * Service implementation for retrieving assets with advanced filtering and sorting capabilities.
 * <p>
 * This service acts as an intermediary between the presentation layer and the data access layer,
 * providing a robust method to fetch assets based on multiple criteria. It supports: - Filtering by
 * upload date range - Filtering by filename - Filtering by file type - Sorting assets by upload
 * date
 * <p>
 * Key Responsibilities: - Transform raw data retrieval requests into concrete filter operations -
 * Apply sorting logic to retrieved assets - Handle and convert low-level exceptions to
 * domain-specific exceptions
 * <p>
 * Uses a reactive approach with Project Reactor's Flux for efficient, non-blocking data retrieval.
 */
public class GetAssetsByFilterService implements IGetAssetsByFilterService {

  /**
   * Adapter for retrieving assets from the underlying data source. Provides abstraction and
   * decoupling from specific data access implementations.
   */
  private final IGetAssetsByFilterAdapter adapter;

  /**
   * Logger for recording service-level events and potential issues. Enables detailed tracing and
   * monitoring of asset retrieval operations.
   */
  private final ILogger log;

  /**
   * Constructs a GetAssetsByFilterService with necessary dependencies.
   *
   * @param adapter       Adapter for asset retrieval operations
   * @param loggerFactory Factory for creating loggers
   */
  public GetAssetsByFilterService(IGetAssetsByFilterAdapter adapter, LoggerFactory loggerFactory) {
    this.adapter = adapter;
    this.log = loggerFactory.getLogger(GetAssetsByFilterService.class);
  }

  /**
   * Retrieves assets based on specified filter and sorting criteria.
   * <p>
   * Performs a multistep process: 1. Creates a comparator based on upload date 2. Adjusts sorting
   * direction if specified as descending 3. Logs the retrieval attempt 4. Fetches assets using the
   * adapter 5. Applies sorting
   *
   * @param uploadDateStart Lower bound of upload date range
   * @param uploadDateEnd   Upper bound of upload date range
   * @param filename        Filename filter criteria
   * @param filetype        File type filter criteria
   * @param sortDirection   Sorting direction ('asc' or 'desc')
   * @return A Flux of Assets matching the specified criteria
   * @throws GeneralException If an error occurs during asset retrieval
   */
  @Override
  public Flux<Assets> getAssetsByFilter(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype, String sortDirection) throws GeneralException {
    try {
      Comparator<Assets> comparator = Comparator.comparing(Assets::getUploadDate);
      if ("desc".equals(sortDirection)) {
        comparator = comparator.reversed();
      }
      log.info("Retrieving assets");
      return adapter.getAssetsByFilter(uploadDateStart, uploadDateEnd, filename, filetype)
          .sort(comparator);
    } catch (Exception e) {
      throw new GeneralException("ERROR RETRIEVING ASSETS", e);
    }
  }
}