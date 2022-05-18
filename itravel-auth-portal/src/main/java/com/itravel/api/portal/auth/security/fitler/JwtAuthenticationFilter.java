package com.itravel.api.portal.auth.security.fitler;

import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.exception.AccountLockedException;
import com.itravel.api.portal.auth.exception.TokenExpiredException;
import com.itravel.api.portal.auth.exception.TokenInvalidException;
import com.itravel.api.portal.auth.security.principal.AccountPrincipal;
import com.itravel.api.portal.auth.security.service.CustomUserDetailsService;
import com.itravel.api.portal.auth.util.JwtUtils;
import com.itravel.api.portal.auth.util.PayloadUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private PayloadUtils payloadUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);
      logger.info("run jwt filter");

      if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
        String userEmail = jwtUtils.getUserEmailFromToken(jwt);

        AccountPrincipal userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        if (userDetails.getAccountVerified().getStatus() == AccountStatus.LOCKED) {
          throw new AccountLockedException();
        }

        logger.info("create userDetails at filter");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        userDetails.setAccessToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        logger.info("cannot get user from token");
      }

    } catch (TokenInvalidException | TokenExpiredException e) {
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getOutputStream().println(payloadUtils.parseObjectToString(e.getErrorDetail()));
      return;
    } catch (AccountLockedException e) {
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getOutputStream().println(payloadUtils.parseObjectToString(e.getErrorDetail()));
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}
