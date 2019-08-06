package com.sucl.security.security.jwt.properties;

import com.sucl.security.security.jwt.model.AuthenticationRequestBody;
import com.sucl.security.security.jwt.model.SimpleAuthenticationRequestBody;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * Configuration properties for jwt security
 * Created by SBratic on 2/17/2017.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "com.github.cobrijani.jwt", ignoreUnknownFields = false)
public class JwtSecurityProperties {

  private Token token = new Token();

  private Class<? extends AuthenticationRequestBody> authenticationRequestBody = SimpleAuthenticationRequestBody.class;

  private boolean enabled = true;

  private String url = "/api/v1/login";

  @Getter
  @Setter
  public static class Token {

    private String tokenHeader = "Authorization";

    private String tokenSchema = "Bearer ";

    private String secret = UUID.randomUUID().toString();

    private long tokenValidityInSeconds = 1800;

    private long tokenValidityInSecondsForRememberMe = 2592000;

    private Payload payload = new Payload();

    @Getter
    @Setter
    public static class Payload {

      private String authoritiesKey = "auth";

      private String loginKey = "login";

    }
  }


}
