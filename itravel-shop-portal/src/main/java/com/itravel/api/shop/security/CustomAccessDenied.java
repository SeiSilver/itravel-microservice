package com.itravel.api.shop.security;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorDetail;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.util.PayloadUtils;
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
      AccessDeniedException e) throws IOException {
    log.error("Access denied - {}", e.getMessage());
    response.setStatus(HttpStatus.FORBIDDEN.value());
    ErrorDetail errorDetail = ErrorDetail.builder().errorCode(ErrorCode.ACCESS_DENIED)
        .errorMessage(ErrorMessage.ACCESS_DENIED).build();
    response.setContentType("application/json");
    response.getOutputStream().println(payloadUtils.parseObjectToString(errorDetail));
  }

}
