package interview.assets.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "interview.assets.demo.persistence.mongo.repositories")
public class MongoConfig {
  // Additional MongoDB configuration if needed
}