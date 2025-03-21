package interview.assets.demo.api.controllers;

import interview.assets.demo.api.IApiAssetsRequestMapper;
import interview.assets.demo.api.dtos.AssetFileUploadRequest;
import interview.assets.demo.api.dtos.AssetFileUploadResponse;
import interview.assets.demo.domain.interfaces.IAssetsRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/mgmt/1/assets")
@AllArgsConstructor
@Tag(name = "Assets", description = "Assets management API")
public class AssetsController {

  private final IAssetsRequestService assetsService;
  private final IApiAssetsRequestMapper apiAssetsMapper;

  @Operation(
      summary = "Upload an asset file",
      description = "Upload an asset file and return the asset ID"
  )
  @ApiResponse(responseCode = "202", description = "Asset uploaded successfully",
      content = @Content(schema = @Schema(implementation = AssetFileUploadResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input")
  @ApiResponse(responseCode = "500", description = "Internal server error")
  @PostMapping("/actions/upload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<AssetFileUploadResponse> uploadAssetFile(
      @RequestBody AssetFileUploadRequest request) {
    return assetsService.uploadAsset(apiAssetsMapper.toDomain(request))
        .map(AssetFileUploadResponse::new);
  }

  @Operation(
      summary = "Health check",
      description = "Check if the API is running"
  )
  @ApiResponse(responseCode = "200", description = "API is running",
      content = @Content(schema = @Schema(implementation = String.class)))
  @ApiResponse(responseCode = "500", description = "Internal server error")
  @GetMapping
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Assets API is running");
  }
}
