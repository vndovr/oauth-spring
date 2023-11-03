package eu.ibagroup.oauth2.example.login;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "LoginClient",
    url = "http://localhost:8081/realms/TEST/protocol/openid-connect",
    configuration = LoginClientConfiguration.class)
public interface AuthClient {

  @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  TokenResponse authorize(@RequestBody TokenRequest body);

  @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  TokenResponse refresh(@RequestBody RefreshRequest body);

  @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded", produces = "application/json")
  ResponseEntity<Void> revoke(@RequestBody RevokeRequest body);

}
