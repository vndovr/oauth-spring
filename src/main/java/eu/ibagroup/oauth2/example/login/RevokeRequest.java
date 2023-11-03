package eu.ibagroup.oauth2.example.login;

import feign.form.FormProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevokeRequest {
  @FormProperty("client_id")
  private String clientId;
  @FormProperty("client_secret")
  private String clientSecret;
  @FormProperty("token")
  private String token;
}
