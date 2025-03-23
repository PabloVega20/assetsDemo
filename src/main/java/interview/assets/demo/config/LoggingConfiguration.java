package interview.assets.demo.config;

import interview.assets.demo.domain.interfaces.ILogger;
import interview.assets.demo.infraestructure.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

  @Bean
  public LoggerFactory loggerFactory() {
    return new LoggerFactory();
  }

  public static class LoggerFactory {

    public ILogger getLogger(Class<?> clazz) {
      return new Slf4jLogger(clazz);
    }
  }
}