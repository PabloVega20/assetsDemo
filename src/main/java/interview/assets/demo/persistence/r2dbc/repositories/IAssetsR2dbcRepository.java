package interview.assets.demo.persistence.r2dbc.repositories;

import interview.assets.demo.persistence.r2dbc.entities.AssetsEntity;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IAssetsR2dbcRepository extends R2dbcRepository<AssetsEntity, Long> {

  @Query("SELECT * FROM assets WHERE " +
      "(:filename IS NULL OR filename LIKE CONCAT('%', :filename, '%')) AND " +
      "(:contentType IS NULL OR content_type = :contentType) AND " +
      "(:startDate IS NULL OR upload_date >= :startDate) AND " +
      "(:endDate IS NULL OR upload_date <= :endDate) AND " +
      "ORDER BY upload_date :order")
  Flux<AssetsEntity> findByDynamicFilters(
      @Param("filename") String filename,
      @Param("contentType") String contentType,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      @Param("order") String order
  );
}
