package interview.assets.demo.domain.interfaces;

import interview.assets.demo.domain.objects.Assets;
import interview.assets.demo.persistence.r2dbc.entities.AssetsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAssetsMapper {

  Assets toDomain(AssetsEntity entity);
}
