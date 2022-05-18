package com.itravel.api.portal.auth.security;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;
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
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final String ERROR_CODE = "ErrorCode";
  private static final String ERROR_MESSAGE = "ErrorMessage";

  private final PayloadUtils payloadUtils;

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e) throws IOException {
    log.error("Responding with unauthorized error. Message - {}", e.getMessage());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    ErrorDetail errorDetail = ErrorDetail.builder()
        .errorCode(ErrorCode.AUTHORIZED_FAIL)
        .errorMessage(ErrorMessage.AUTHORIZED_FAIL)
        .build();
    response.setContentType("application/json");
    response.getOutputStream().println(payloadUtils.parseObjectToString(errorDetail));
    log.error("jwt login fail!: {}", errorDetail.getErrorMessage(), e);

  }

}
