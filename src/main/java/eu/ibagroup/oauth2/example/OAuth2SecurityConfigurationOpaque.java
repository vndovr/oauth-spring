package eu.ibagroup.oauth2.example;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
@Profile("opaque")
public class OAuth2SecurityConfigurationOpaque {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.GET,
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/actuator/**",
            "/login",
            "/auth",
            "/refresh",
            "/revoke")
        .anonymous()
        .requestMatchers(HttpMethod.POST, "/token**")
        .anonymous()
        .anyRequest()
        .authenticated())
        .oauth2ResourceServer((oauth2) -> oauth2.opaqueToken(Customizer.withDefaults()))
        .sessionManagement(
            (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  public OpaqueTokenIntrospector introspector(ObjectMapper objectMapper,
      @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}") String interospectUrl,
      @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}") String clientId,
      @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}") String clientSecret) {
    return new OpaqueTokenIntrospector() {

      private OpaqueTokenIntrospector delegate =
          new NimbusOpaqueTokenIntrospector(interospectUrl, clientId, clientSecret);

      @SneakyThrows
      public OAuth2AuthenticatedPrincipal introspect(String token) {

        log.info("Custom instrospector called");
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        log.info("Returned principal: {}",
            objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(principal.getAttributes()));

        List<String> scopes = principal.getAttribute("scope");
        Collection<GrantedAuthority> grantedAuthorities =
            scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new DefaultOAuth2AuthenticatedPrincipal(principal.getName(),
            principal.getAttributes(),
            grantedAuthorities);
      }
    };
  }
}
