package interview.assets.demo.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import interview.assets.demo.config.LoggingConfiguration.LoggerFactory;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterAdapter;
import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetAssetsByFilterServiceTest {

  @Mock
  private IGetAssetsByFilterAdapter adapter;

  @Mock
  private LoggerFactory loggerFactory;

  @Mock
  private ILogger logger;

  private GetAssetsByFilterService service;

  @BeforeEach
  void setUp() {
    when(loggerFactory.getLogger(GetAssetsByFilterService.class)).thenReturn(logger);
    service = new GetAssetsByFilterService(adapter, loggerFactory);
  }

  @Test
  @DisplayName("Retrieve assets with ascending sort order")
  void testGetAssetsByFilterAscending() throws GeneralException {
    LocalDateTime start = LocalDateTime.now().minusDays(1);
    LocalDateTime end = LocalDateTime.now();
    String filename = "test.txt";
    String filetype = "txt";

    Assets asset1 = new Assets();
    asset1.setUploadDate(start);
    Assets asset2 = new Assets();
    asset2.setUploadDate(end);

    when(adapter.getAssetsByFilter(
        start, end, filename, filetype)
    ).thenReturn(Flux.just(asset2, asset1));

    StepVerifier.create(service.getAssetsByFilter(start, end, filename, filetype, "asc"))
        .expectNextMatches(asset -> asset.getUploadDate().equals(start))
        .expectNextMatches(asset -> asset.getUploadDate().equals(end))
        .verifyComplete();

    verify(logger).info("Retrieving assets");
  }

  @Test
  @DisplayName("Retrieve assets with descending sort order")
  void testGetAssetsByFilterDescending() throws GeneralException {
    LocalDateTime start = LocalDateTime.now().minusDays(1);
    LocalDateTime end = LocalDateTime.now();
    String filename = "test.txt";
    String filetype = "txt";

    Assets asset1 = new Assets();
    asset1.setUploadDate(start);
    Assets asset2 = new Assets();
    asset2.setUploadDate(start);

    when(adapter.getAssetsByFilter(
        start, end, filename, filetype)
    ).thenReturn(Flux.just(asset1, asset2));

    StepVerifier.create(service.getAssetsByFilter(start, end, filename, filetype, "desc"))
        .expectNextMatches(asset -> asset.getUploadDate().equals(start))
        .expectNextMatches(asset -> asset.getUploadDate().equals(start))
        .verifyComplete();

    verify(logger).info("Retrieving assets");
  }

  @Test
  @DisplayName("Throws GeneralException when adapter fails")
  void testGetAssetsByFilterException() {
    LocalDateTime start = LocalDateTime.now().minusDays(1);
    LocalDateTime end = LocalDateTime.now();
    String filename = "test.txt";
    String filetype = "txt";

    when(adapter.getAssetsByFilter(
        start, end, filename, filetype)
    ).thenThrow(new RuntimeException("Test exception"));

    GeneralException exception = assertThrows(GeneralException.class, () -> {
      service.getAssetsByFilter(start, end, filename, filetype, "asc");
    });

    assert exception.getMessage().contains("ERROR RETRIEVING ASSETS");

    verify(logger).info("Retrieving assets");
  }

  @Test
  @DisplayName("Handles empty result set")
  void testGetAssetsByFilterEmptyResult() throws GeneralException {
    LocalDateTime start = LocalDateTime.now().minusDays(1);
    LocalDateTime end = LocalDateTime.now();
    String filename = "test.txt";
    String filetype = "txt";

    when(adapter.getAssetsByFilter(
        start, end, filename, filetype)
    ).thenReturn(Flux.empty());

    StepVerifier.create(service.getAssetsByFilter(start, end, filename, filetype, "asc"))
        .verifyComplete();

    verify(logger).info("Retrieving assets");
  }
}