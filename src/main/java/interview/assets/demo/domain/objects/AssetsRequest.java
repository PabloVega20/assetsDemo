package interview.assets.demo.domain.objects;

import lombok.Data;

@Data
public class AssetsRequest {

  private String fileName;
  private String encodedFile; // Base64
  private String contentType;
}
