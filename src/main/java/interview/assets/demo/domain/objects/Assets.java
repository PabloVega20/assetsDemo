package interview.assets.demo.domain.objects;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assets {

  private String id;
  private String filename;
  private String contentType;
  private String url;
  private Long size;
  private LocalDateTime uploadDate;
}
