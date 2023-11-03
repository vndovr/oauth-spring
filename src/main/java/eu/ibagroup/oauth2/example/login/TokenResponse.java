package eu.ibagroup.oauth2.example.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("expires_in")
  private int expiresIn;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("refresh_expires_in")
  private int refreshExpiresIn;
  @JsonProperty("not-before-policy")
  private int notBeforePolicy;
  @JsonProperty("session_state")
  private String sessionState;
  @JsonProperty("scope")
  private String scope;
  @JsonProperty("error")
  private String error;
  @JsonProperty("error_description")
  private String errorDescription;

}
