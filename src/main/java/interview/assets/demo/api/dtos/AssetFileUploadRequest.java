package interview.assets.demo.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Asset file upload request")
public class AssetFileUploadRequest {

  @Schema(description = "Name of the asset", example = "profile-image")
  private String name;

  @Schema(description = "Type of the asset", example = "image/jpeg")
  private String type;

  @Schema(description = "Base64 encoded content of the asset", example = "base64EncodedString")
  private String content;
}
