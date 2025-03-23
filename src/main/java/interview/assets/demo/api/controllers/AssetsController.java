package interview.assets.demo.api.controllers;

import interview.assets.demo.api.dtos.AssetsDTO;
import interview.assets.demo.api.dtos.AssetsFileUploadRequest;
import interview.assets.demo.api.dtos.AssetsFileUploadResponse;
import interview.assets.demo.api.mappers.IApiAssetsMapper;
import interview.assets.demo.api.mappers.IApiAssetsRequestMapper;
import interview.assets.demo.domain.interfaces.IAssetsRequestService;
import interview.assets.demo.domain.interfaces.IGetAssetsByFilterService;
import interview.assets.demo.domain.objects.exceptions.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/mgmt/1/assets")
@AllArgsConstructor
@Tag(name = "Assets", description = "Assets management API")
public class AssetsController {

  private final IAssetsRequestService assetsRequestService;
  private final IApiAssetsRequestMapper apiAssetsRequestMapper;
  private final IApiAssetsMapper apiAssetsMapper;
  private final IGetAssetsByFilterService getAssetsByFilterService;

  @Operation(
      summary = "Upload an asset file",
      description = "Upload an asset file and return the asset ID"
  )
  @ApiResponse(responseCode = "202", description = "Asset uploaded successfully",
      content = @Content(schema = @Schema(implementation = AssetsFileUploadResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input")
  @ApiResponse(responseCode = "500", description = "Internal server error")
  @PostMapping("/actions/upload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<AssetsFileUploadResponse> uploadAssetFile(
      @RequestBody AssetsFileUploadRequest request) throws GeneralException {
    return assetsRequestService.uploadAsset(apiAssetsRequestMapper.toDomain(request))
        .map(AssetsFileUploadResponse::new);
  }

  @Operation(
      summary = "Get assets by filter criteria",
      description = "Retrieves assets based on optional filter criteria. If no filters are provided, returns all assets."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved assets",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AssetsDTO.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Invalid filter parameters provided",
          content = @Content
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error",
          content = @Content
      )
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<AssetsDTO> getAssets(
      @Parameter(description = "Filter assets by filename (partial match, case-sensitive)")
      @RequestParam(required = false) String filename,

      @Parameter(description = "Filter assets by content type (exact match, e.g., 'image/jpeg')")
      @RequestParam(required = false) String contentType,

      @Parameter(description = "Filter assets uploaded on or after this date-time (ISO format: yyyy-MM-dd'T'HH:mm:ss'Z')")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

      @Parameter(description = "Filter assets uploaded on or before this date-time (ISO format: yyyy-MM-dd'T'HH:mm:ss'Z')")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

      @Parameter(description = "SortDirection of uploadDate of the Assets")
      @RequestParam(required = false) String sortDirection
  ) throws GeneralException {
    return getAssetsByFilterService.getAssetsByFilter(startDate, endDate,
        filename, contentType, sortDirection
    ).map(apiAssetsMapper::toDTO);
  }

}
