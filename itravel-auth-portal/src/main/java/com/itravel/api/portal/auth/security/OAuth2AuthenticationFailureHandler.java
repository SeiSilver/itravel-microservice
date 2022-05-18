package com.itravel.api.portal.auth.security;

import com.itravel.api.portal.auth.error.ErrorDetail;
import com.itravel.api.portal.auth.util.PayloadUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final PayloadUtils payloadUtils;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception)
      throws IOException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    ErrorDetail errorDetail = ErrorDetail
        .builder()
        .errorCode(HttpStatus.UNAUTHORIZED.value())
        .errorMessage(exception.getMessage())
        .build();
    response.setContentType("application/json");
    response.getOutputStream().println(payloadUtils.parseObjectToString(errorDetail));

    log.error("oath2 login fail: {}", exception.getMessage(), exception);

  }

}
