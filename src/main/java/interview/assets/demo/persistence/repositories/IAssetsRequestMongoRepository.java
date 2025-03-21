package interview.assets.demo.persistence.repositories;

import interview.assets.demo.persistence.documents.AssetsRequestDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAssetsRequestMongoRepository extends
    ReactiveMongoRepository<AssetsRequestDocument, String> {

}
