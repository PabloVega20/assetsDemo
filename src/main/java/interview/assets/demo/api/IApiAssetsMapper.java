package interview.assets.demo.api;

import interview.assets.demo.api.dtos.AssetsDTO;
import interview.assets.demo.domain.objects.Assets;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IApiAssetsMapper {

  AssetsDTO toDTO(Assets request);
}
