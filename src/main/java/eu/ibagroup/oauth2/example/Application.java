package eu.ibagroup.oauth2.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
}
