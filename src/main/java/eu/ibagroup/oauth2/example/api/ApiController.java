package eu.ibagroup.oauth2.example.api;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "API controller")
@AllArgsConstructor
public class ApiController {

  private ObjectMapper objectMapper;

  @GetMapping(path = "/self", produces = "text/plain")
  @Operation(description = "Call the API /self to get information about user")
  @SneakyThrows
  public ResponseEntity<String> log(Authentication authentication) {
    
    log.info("/self user information");

    String json = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(Map
            .of("name", authentication.getName(), "authorities", authentication.getAuthorities()));

    log.info("Authentication: {}", json);
    return ResponseEntity.ok(json);
  }


  @GetMapping(path = "/manager")
  @Operation(description = "Call the API that is accessible only by user with role MANAGER")
  @RolesAllowed("MANAGER")
  public ResponseEntity<String> manager() {
    log.info("Managers endpoint called");
    return ResponseEntity.ok("Access to /manager endpoint");
  }

}
