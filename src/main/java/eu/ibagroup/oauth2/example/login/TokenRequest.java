package eu.ibagroup.oauth2.example.login;

import feign.form.FormProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
  @FormProperty("client_id")
  private String clientId;
  @FormProperty("client_secret")
  private String clientSecret;
  @FormProperty("redirect_uri")
  private String redirectUri;
  @FormProperty("grant_type")
  private String grantType;
  @FormProperty("code")
  private String code;
}
