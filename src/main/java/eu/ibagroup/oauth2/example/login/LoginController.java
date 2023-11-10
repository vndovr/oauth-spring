package eu.ibagroup.oauth2.example.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Login controller")
@RequiredArgsConstructor
public class LoginController {

  private final AuthClient authClient;
  private final ObjectMapper objectMapper;

  @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}")
  private String clientSecret;
  
  @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}")
  private String clientId;

  @GetMapping(path = "/login")
  @Operation(description = "Perform the login - 1st step - get authorization code grant")
  public ResponseEntity<String> login() {

    log.info("Login request");

    HttpHeaders headers = new HttpHeaders();
    headers.add("Location",
        String.format("http://localhost:8081/realms/TEST/protocol/openid-connect/auth?client_id=%s&redirect_uri=http://localhost:8080/auth&response_type=code", clientId));

    return new ResponseEntity<String>(null, headers, HttpStatus.FOUND);
  }

  @GetMapping(path = "/auth", produces = "text/plain")
  @Operation(description = "Perform the login - 2nd step - get tokens")
  @SneakyThrows
  public ResponseEntity<String> login(@RequestParam("code") String code,
      HttpServletResponse httpServletResponse) {

    log.info("Authorization code request: {}", code);

    TokenResponse result = authClient.authorize(TokenRequest.builder()
        .clientId(clientId)
        .code(code)
        .grantType("authorization_code")
        .redirectUri("http://localhost:8080/auth")
        .clientSecret(clientSecret)
        .build());

    log.info("Response: {}", result);

    return ResponseEntity
        .ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
  }

  @GetMapping(path = "/refresh", produces = "text/plain")
  @Operation(description = "Refresh the token")
  @SneakyThrows
  public ResponseEntity<String> refresh(@RequestParam("token") String token) {

    log.info("Request to refresh the token: {}", token);

    TokenResponse result = authClient.refresh(RefreshRequest.builder()
        .clientId(clientId)
        .refreshToken(token)
        .grantType("refresh_token")
        .clientSecret(clientSecret)
        .build());

    log.info("Response: {}", result);

    return ResponseEntity
        .ok(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
  }

  @GetMapping(path = "/revoke", produces = "text/plain")
  @Operation(description = "Revoke the token")
  @SneakyThrows
  public ResponseEntity<String> revoke(@RequestParam("token") String token) {

    log.info("Request to revoke the token: {}", token);

    ResponseEntity<Void> result = authClient.revoke(RevokeRequest.builder()
        .clientId(clientId)
        .clientSecret(clientSecret)
        .token(token)
        .build());

    log.info("Response http status code: {}", result.getStatusCode());

    return ResponseEntity.status(result.getStatusCode()).build();
  }
}
