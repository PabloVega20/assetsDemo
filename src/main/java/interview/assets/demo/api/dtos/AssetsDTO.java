package interview.assets.demo.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsDTO {

  private String id;
  private String filename;
  private String contentType;
  private String url;
  private Integer size;
  private String uploadDate;
}
