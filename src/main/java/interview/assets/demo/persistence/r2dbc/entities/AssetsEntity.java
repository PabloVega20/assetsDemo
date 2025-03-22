package interview.assets.demo.persistence.r2dbc.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("ASSETS")
public class AssetsEntity {

  @Id
  private Long id;

  @Column
  private String filename;

  @Column("content_type")
  private String contentType;

  @Column
  private String url;

  @Column
  private Integer size;

  @Column("upload_date")
  private LocalDateTime uploadDate;
  // Note: Binary content handling is different in R2DBC
  // You might need to store it separately or use a specific R2DBC driver feature
}
