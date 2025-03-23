package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

public interface IGetAssetsByFilterService {

  Flux<Assets> getAssetsByFilter(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype, String sortDirection) throws GeneralException;
}
