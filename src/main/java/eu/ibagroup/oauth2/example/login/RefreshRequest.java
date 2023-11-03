package eu.ibagroup.oauth2.example.login;

import feign.form.FormProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshRequest {
  @FormProperty("client_id")
  private String clientId;
  @FormProperty("client_secret")
  private String clientSecret;
  @FormProperty("grant_type")
  private String grantType;
  @FormProperty("refresh_token")
  private String refreshToken;
}
