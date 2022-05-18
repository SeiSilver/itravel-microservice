package com.itravel.api.shop.security;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorDetail;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.util.CookieUtils;
import com.itravel.api.shop.util.PayloadUtils;
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

    String errorCode = CookieUtils.getValueByName(request.getCookies(), ERROR_CODE);
    String errorMessage = CookieUtils.getValueByName(request.getCookies(), ERROR_MESSAGE);

    CookieUtils.deleteCookie(request, response, ERROR_CODE);
    CookieUtils.deleteCookie(request, response, ERROR_MESSAGE);

    if (errorCode == null || errorMessage == null) {
      errorCode = String.valueOf(ErrorCode.AUTHORIZED_FAIL);
      errorMessage = ErrorMessage.AUTHORIZED_FAIL;
    }

    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    ErrorDetail errorDetail = ErrorDetail.builder().errorCode(Integer.valueOf(errorCode))
        .errorMessage(errorMessage).build();
    response.setContentType("application/json");
    response.getOutputStream().println(payloadUtils.parseObjectToString(errorDetail));

    log.error("jwt login fail!: {}", errorDetail.getErrorMessage(), e);

  }

}
