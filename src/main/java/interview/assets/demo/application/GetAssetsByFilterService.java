package interview.assets.demo.application;

import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterService;
import interview.assets.demo.domain.objects.Assets;
import java.time.LocalDateTime;
import java.util.Comparator;
import reactor.core.publisher.Flux;

public class GetAssetsByFilterService implements IGetAssetsByFilterService {

  private final IGetAssetsByFilterAdapter adapter;

  public GetAssetsByFilterService(IGetAssetsByFilterAdapter adapter) {
    this.adapter = adapter;
  }

  @Override
  public Flux<Assets> getAssetsByFilter(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype, String sortDirection) {
    Comparator<Assets> comparator = Comparator.comparing(Assets::getUploadDate);
    if ("desc".equals(sortDirection)) {
      comparator.reversed();
    }
    return adapter.getAssetsByFilter(uploadDateStart, uploadDateEnd, filename, filetype)
        .sort(comparator);
  }
}
