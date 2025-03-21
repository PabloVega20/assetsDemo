package interview.assets.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = {"interview.assets.demo.persistence.repositories"})
public class AssetsDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(AssetsDemoApplication.class, args);
  }

}
