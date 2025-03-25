package interview.assets.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8080");
    devServer.setDescription("Server URL in Development environment");

    Contact contact = new Contact();
    contact.setEmail("info@assets-demo.com");
    contact.setName("Assets Demo API");
    contact.setUrl("https://www.assets-demo.com");

    License mitLicense = new License()
        .name("MIT License")
        .url("https://choosealicense.com/licenses/mit/");

    Info info = new Info()
        .title("Assets Management API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints to manage assets.")
        .license(mitLicense);

    return new OpenAPI()
        .info(info)
        .servers(List.of(devServer));
  }
}
