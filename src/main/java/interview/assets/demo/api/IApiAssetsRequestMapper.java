package interview.assets.demo.api;

import interview.assets.demo.api.dtos.AssetFileUploadRequest;
import interview.assets.demo.domain.objects.AssetsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IApiAssetsRequestMapper {

  @Mapping(target = "encodedFile", source = "content")
  @Mapping(target = "contentType", source = "type")
  @Mapping(target = "fileName", source = "name")
  AssetsRequest toDomain(AssetFileUploadRequest request);
}
