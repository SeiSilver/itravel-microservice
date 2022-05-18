package com.itravel.api.payment.config;

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

  @NotNull
  @Valid
  private final PaymentPortal paymentPortal;

  @NotNull
  @Valid
  private final ShopPortal shopPortal;

  @NotNull
  @Valid
  private final UxConfig ux;

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

  @Getter
  @RequiredArgsConstructor
  public static class PaymentPortal {

    @NotBlank
    private final String baseUrl;

  }

  @Getter
  @RequiredArgsConstructor
  public static class ShopPortal {

    @NotBlank
    private final String baseUrl;

    @NotNull
    private final String shopOwner;

  }

  @Getter
  @RequiredArgsConstructor
  public static class UxConfig {

    @NotBlank
    private final String baseUrl;

  }
}
