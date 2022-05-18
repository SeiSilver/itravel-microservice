package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.annotation.CurrentUser;
import com.itravel.api.portal.auth.constraint.CommonKey;
import com.itravel.api.portal.auth.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.portal.auth.exception.TokenExpiredException;
import com.itravel.api.portal.auth.exception.TokenInvalidException;
import com.itravel.api.portal.auth.model.dto.JwtResponse;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.util.CookieUtils;
import com.itravel.api.portal.auth.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(AuthPortalEndPoint.AUTH)
@RequiredArgsConstructor
public class AuthController {

  private static final String GOOGLE_URI = "/oauth2/authorization/google";

  private static final String REDIRECT_PREFIX = "redirect:";

  private final JwtUtils jwtUtils;

  @GetMapping(AuthPortalEndPoint.AUTH_LOGIN)
  @ApiIgnore
  public String login(@RequestParam(value = "redirectUri", required = false) String redirectUri,
      HttpServletResponse response) {

    if (redirectUri == null || redirectUri.isEmpty()) {
      redirectUri = AuthPortalEndPoint.AUTH + AuthPortalEndPoint.AUTH_TOKEN;
    }

    CookieUtils.addCookie(response, "redirectUri", redirectUri, 360);

    String loginUrl = UriComponentsBuilder.fromUriString(GOOGLE_URI)
        .queryParam("redirectUri", redirectUri)
        .build().toUriString();

    return REDIRECT_PREFIX + loginUrl;
  }

  @ResponseBody
  @GetMapping(AuthPortalEndPoint.AUTH_TOKEN)
  @PreAuthorize("hasAnyRole('USER', 'SELLER', 'ADMIN', 'MORDERATOR')")
  @ApiOperation(value = "This method is used to get the access token info.")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public JwtResponse getJwtInfo(@ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @ApiIgnore HttpServletRequest request) {
    request.getSession().invalidate();

    return jwtUtils.getTokenInfo(userPrincipal.getAccessToken());
  }

  @ResponseBody
  @GetMapping(AuthPortalEndPoint.AUTH_VERIFY)
  @ApiOperation(value = "This method is used verify token.")
  public JwtResponse verifyToken(@RequestParam("token") String token)
      throws TokenInvalidException, TokenExpiredException {

    if (StringUtils.hasText(token)) {
      jwtUtils.validateToken(token);
    } else {
      throw new TokenInvalidException();
    }

    return jwtUtils.getTokenInfo(token);
  }

  @ApiIgnore
  @ResponseBody
  @GetMapping(AuthPortalEndPoint.AUTH_ACCOUNT)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "Get current AccountEntity for other API Service")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public AccountEntity getCurrentUserBasicInfo(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal) {
    return userPrincipal.getAccountVerified();
  }

}
