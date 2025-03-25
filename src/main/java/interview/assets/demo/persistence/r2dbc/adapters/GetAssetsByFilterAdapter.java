package interview.assets.demo.persistence.r2dbc.adapters;

import interview.assets.demo.domain.interfaces.IAssetsMapper;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.persistence.r2dbc.entities.AssetsEntity;
import interview.assets.demo.persistence.r2dbc.repositories.IAssetsR2dbcRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adapter implementation for retrieving and saving assets using R2DBC repository.
 * <p>
 * This class serves as a bridge between the domain logic and the R2DBC database repository,
 * providing reactive operations for filtering, retrieving, and saving asset information.
 * <p>
 * The adapter implements the {@link IGetAssetsByFilterAdapter} interface, enabling flexible and
 * efficient data access with reactive programming support.
 * <p>
 * Key Responsibilities: - Filter assets based on dynamic criteria - Map between database entities
 * and domain objects - Perform asset persistence operations
 *
 * @see IAssetsR2dbcRepository
 * @see IAssetsMapper
 */
@Component
@AllArgsConstructor
@Slf4j
public class GetAssetsByFilterAdapter implements IGetAssetsByFilterAdapter {

  /**
   * R2DBC repository for performing database operations on assets. Provides reactive database
   * access for asset-related queries.
   */
  private final IAssetsR2dbcRepository assetsR2dbcRepository;

  /**
   * Mapper responsible for converting between database entities and domain objects. Ensures clean
   * separation between persistence and domain layers.
   */
  private final IAssetsMapper assetsMapper;

  /**
   * Retrieves assets based on dynamic filtering criteria.
   * <p>
   * Allows filtering assets by: - Filename - File type - Upload date range
   *
   * @param uploadDateStart Lower bound of upload date range
   * @param uploadDateEnd   Upper bound of upload date range
   * @param filename        Partial or full filename to filter
   * @param filetype        File type to filter
   * @return A {@link Flux} of {@link Assets} matching the specified criteria
   */
  @Override
  public Flux<Assets> getAssetsByFilter(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype) {
    return assetsR2dbcRepository.findByDynamicFilters(filename,
            filetype, uploadDateStart, uploadDateEnd)
        .map(assetsMapper::toDomain);
  }

  /**
   * Saves an asset to the database.
   * <p>
   * Converts the domain object to a database entity and persists it. Logs the entity being saved
   * for traceability.
   *
   * @param assets The asset to be saved
   * @return A {@link Mono<Void>} representing the completion of the save operation
   */
  @Override
  public Mono<Void> save(Assets assets) {
    AssetsEntity entity = assetsMapper.toEntity(assets);
    log.info("Saving assets: {}", entity);
    return assetsR2dbcRepository.save(entity).then();
  }
}