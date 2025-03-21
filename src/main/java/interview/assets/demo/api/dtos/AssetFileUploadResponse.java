package interview.assets.demo.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Asset file upload response")
public class AssetFileUploadResponse {

  @Schema(description = "ID of the uploaded asset", example = "60f1e5b5e8f87c001234abcd")
  private String id;
}
