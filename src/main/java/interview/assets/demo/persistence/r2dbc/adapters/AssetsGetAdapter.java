package interview.assets.demo.persistence.r2dbc.adapters;

import interview.assets.demo.domain.interfaces.IAssetGetAdapter;
import interview.assets.demo.domain.interfaces.IAssetsMapper;
import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.persistence.r2dbc.repositories.IAssetsR2dbcRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class AssetsGetAdapter implements IAssetGetAdapter {

  private final IAssetsR2dbcRepository assetsR2dbcRepository;
  private final IAssetsMapper assetsMapper;

  @Override
  public Flux<Assets> getAsset(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype, SortDirection sortDirection) {
    return assetsR2dbcRepository.findByDynamicFilters(filename,
        filetype, uploadDateStart, uploadDateEnd, sortDirection.name()
    ).map(assetsMapper::toDomain);
  }
}
