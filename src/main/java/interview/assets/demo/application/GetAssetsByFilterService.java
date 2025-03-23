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

public class GetAssetsByFilterService implements IGetAssetsByFilterService {

  private final IGetAssetsByFilterAdapter adapter;
  private final ILogger log;

  public GetAssetsByFilterService(IGetAssetsByFilterAdapter adapter, LoggerFactory loggerFactory) {
    this.adapter = adapter;
    this.log = loggerFactory.getLogger(GetAssetsByFilterService.class);
  }

  @Override
  public Flux<Assets> getAssetsByFilter(LocalDateTime uploadDateStart, LocalDateTime uploadDateEnd,
      String filename, String filetype, String sortDirection) throws GeneralException {
    try {
      Comparator<Assets> comparator = Comparator.comparing(Assets::getUploadDate);
      if ("desc".equals(sortDirection)) {
        comparator.reversed();
      }
      log.info("Retrieving assets");
      return adapter.getAssetsByFilter(uploadDateStart, uploadDateEnd, filename, filetype)
          .sort(comparator);
    } catch (Exception e) {
      throw new GeneralException("ERROR RETRIEVING ASSETS", e);
    }
  }
}
