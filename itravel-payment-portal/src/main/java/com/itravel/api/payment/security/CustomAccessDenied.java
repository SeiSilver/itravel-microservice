package com.itravel.api.payment.security;

import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;
import com.itravel.api.payment.error.ErrorDetail;
import com.itravel.api.payment.util.PayloadUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class CustomAccessDenied implements AccessDeniedHandler {

  private final PayloadUtils payloadUtils;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException e) throws IOException, ServletException {
    log.error("Access denied - {}", e.getMessage());
    response.setStatus(HttpStatus.FORBIDDEN.value());
    ErrorDetail errorDetail = ErrorDetail.builder().errorCode(ErrorCode.ACCESS_DENIED)
        .errorMessage(ErrorMessage.ACCESS_DENIED).build();
    response.setContentType("application/json");
    response.getOutputStream().println(payloadUtils.parseObjectToString(errorDetail));
  }

}
