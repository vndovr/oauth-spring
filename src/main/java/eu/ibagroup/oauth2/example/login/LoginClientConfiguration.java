package eu.ibagroup.oauth2.example.login;

import org.springframework.context.annotation.Bean;
import feign.Logger;

public class LoginClientConfiguration {

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }


}
