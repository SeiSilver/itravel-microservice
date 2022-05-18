package com.itravel.api.portal.auth.security;

import com.itravel.api.portal.auth.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.util.CookieUtils;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@Log4j2
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    String token = ((AccountPrincipal) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal()).getAccessToken();

    String targetUrl = determineTargetUrl(request, response, token);
    response.addHeader("Authorization", "Bear " + token);
    request.getSession(false).setMaxInactiveInterval(120);

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  @SneakyThrows
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
      String token) {
    Optional<String> redirectUri = CookieUtils.getCookieByName(request, "redirectUri")
        .map(Cookie::getValue);

    String targetUrl = redirectUri.orElse(AuthPortalEndPoint.AUTH + AuthPortalEndPoint.AUTH_TOKEN);
    CookieUtils.deleteCookie(request, response, "redirectUri");

    return UriComponentsBuilder.fromUriString(URLDecoder.decode(targetUrl, StandardCharsets.UTF_8))
        .queryParam("accessToken", token)
        .build().toUriString();
  }


}
