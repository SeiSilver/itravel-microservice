package com.itravel.api.portal.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

  private static final String OR = "|";

  private final ApplicationProperties applicationProperties;

  private ApiKey apiKey() {
    return new ApiKey("Bearer", "Authorization", "header");
  }


  private AuthorizationScope[] authScope() {
    return new AuthorizationScope[]{
        new AuthorizationScope("global", "accessEverything")
    };
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("iTravel Auth Portal Service")
        .description("iTravel Auth Portal Service API.")
        .version("1.0")
        .termsOfServiceUrl("Terms of service")
        .contact(new Contact("iTravel", "", ""))
        .license("License of API")
        .licenseUrl("API license URL")
        .build();
  }

  @SuppressWarnings("deprecation")
  private List<SecurityScheme> securityScheme() {
    GrantType grantType = new AuthorizationCodeGrantBuilder()
        .tokenEndpoint(new TokenEndpoint(applicationProperties.getAuthPortal().getTokenUrl(), "accessToken"))
        .tokenRequestEndpoint(
            new TokenRequestEndpoint(applicationProperties.getAuthPortal().getLoginUrl(),
                applicationProperties.getGoogleConfig().getClientId(),
                applicationProperties.getGoogleConfig().getClientSecret()))
        .build();
    SecurityScheme oauth = new OAuthBuilder().name("Oauth2 Google")
        .grantTypes(List.of(grantType))
        .scopes(Arrays.asList(authScope()))
        .build();

    List<SecurityScheme> securitySchemes = new ArrayList<>();
    securitySchemes.add(oauth);
    securitySchemes.add(apiKey());

    return securitySchemes;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .securitySchemes(securityScheme())
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("com.itravel.api.portal.auth.controller"))
        .paths(PathSelectors.any())
        .build();
  }

}
