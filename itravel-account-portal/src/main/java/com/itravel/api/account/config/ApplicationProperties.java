package com.itravel.api.account.config;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application")
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@Validated
public class ApplicationProperties {

  @NotNull
  @Valid
  private final AuthConfig authConfig;

  @NotNull
  @Valid
  private final GoogleConfig googleConfig;

  @NotNull
  @Valid
  private final AuthPortal authPortal;

  @Getter
  @RequiredArgsConstructor
  public static class AuthConfig {

    @NotBlank
    private final String tokenSecret;

    @NotNull
    private final Integer tokenExpiredSeconds;

  }

  @Getter
  @RequiredArgsConstructor
  public static class GoogleConfig {

    @NotBlank
    private final String clientId;

    @NotBlank
    private final String clientSecret;

  }

  @Getter
  @RequiredArgsConstructor
  public static class AuthPortal {

    @NotBlank
    private final String loginUrl;

    @NotBlank
    private final String tokenUrl;

    @NotBlank
    private final String baseUrl;

  }

}
