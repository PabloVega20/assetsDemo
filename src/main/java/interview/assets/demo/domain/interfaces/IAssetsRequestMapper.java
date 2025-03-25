package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IAssetsRequestMapper {

  @Mapping(target = "encodedFile", source = "encodedFile", qualifiedByName = "toByteArray")
  @Mapping(target = "fileName", source = "fileName")
  AssetsRequestDocument toDocument(AssetsRequest assets);

  @Mapping(target = "size", source = "encodedFile", qualifiedByName = "toSize")
  @Mapping(target = "uploadDate", source = "uploadedAt", qualifiedByName = "toLocalDateTime")
  @Mapping(target = "filename", source = "fileName")
  @Mapping(target = "contentType", source = "contentType")
  @Mapping(target = "id", ignore = true)
  Assets assetsRequestToAssets(AssetsRequestDocument document);

  @Named("toByteArray")
  default byte[] toByteArray(String encodedFile) {
    if (encodedFile == null) {
      return new byte[0];
    }
    return Base64.getDecoder().decode(encodedFile);
  }

  @Named("toBase64String")
  default String toBase64String(byte[] encodedFile) {
    if (encodedFile == null) {
      return null;
    }
    return Base64.getEncoder().encodeToString(encodedFile);
  }

  @Named("toSize")
  default int calculateOriginalSize(byte[] encodedFile) {
    if (encodedFile == null) {
      return 0;
    }
    String base64String = toBase64String(encodedFile);
    int padding = 0;
    if (base64String.endsWith("==")) {
      padding = 2;
    } else if (base64String.endsWith("=")) {
      padding = 1;
    }
    return ((base64String.length() * 3 / 4) - padding);
  }

  @Named("toLocalDateTime")
  default LocalDateTime toLocalDateTime(Instant instant) {
    return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
  }
}
