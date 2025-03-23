package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.AssetsRequest;
import interview.assets.demo.persistence.mongo.documents.AssetsRequestDocument;
import java.util.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IAssetsRequestMapper {

  @Mapping(target = "encodedFile", source = "encodedFile", qualifiedByName = "toByteArray")
  AssetsRequestDocument toDocument(AssetsRequest assets);

  @Mapping(target = "encodedFile", source = "encodedFile", qualifiedByName = "toBase64String")
  AssetsRequest toDomain(AssetsRequestDocument document);

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
}