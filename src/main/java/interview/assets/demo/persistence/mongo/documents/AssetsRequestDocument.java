package interview.assets.demo.persistence.mongo.documents;

import interview.assets.demo.domain.objects.enums.AssetStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assetsRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetsRequestDocument {

  @Id
  private String id;
  private String fileName;
  private byte[] encodedFile; // Binary
  private String contentType;
  private Instant uploadedAt = Instant.now();
  private AssetStatus status = AssetStatus.PENDING; // PENDING = 0; PROCESSED = 1; CANCELED = 2
}