package com.itravel.api.portal.auth.util;

import com.itravel.api.portal.auth.config.ApplicationProperties;
import com.itravel.api.portal.auth.model.dto.JwtResponse;
import com.itravel.api.portal.auth.exception.TokenExpiredException;
import com.itravel.api.portal.auth.exception.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Calendar;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtUtils {

  private static final String TOKEN_TYPE = "Bearer";

  private final ApplicationProperties applicationProperties;

  public JwtResponse getTokenInfo(String token) {

    Claims claims = Jwts.parser()
        .setSigningKey(applicationProperties.getAuthConfig().getTokenSecret())
        .parseClaimsJws(token)
        .getBody();

    return JwtResponse.builder()
        .accessToken(token)
        .tokenType(TOKEN_TYPE)
        .email(claims.getId())
        .expiredDate(CommonUtils.convertUTCDate(claims.getExpiration()))
        .issuedDate(CommonUtils.convertUTCDate(claims.getIssuedAt()))
        .build();
  }

  public String createToken(String email) {

    Date current = new Date();
    Calendar expiredDate = Calendar.getInstance();
    expiredDate.setTime(current);
    expiredDate.add(Calendar.SECOND,
        applicationProperties.getAuthConfig().getTokenExpiredSeconds());

    return Jwts.builder()
        .setId(email)
        .setIssuedAt(current)
        .setExpiration(expiredDate.getTime())
        .signWith(SignatureAlgorithm.HS256, applicationProperties.getAuthConfig().getTokenSecret())
        .compact();
  }

  public String getUserEmailFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(applicationProperties.getAuthConfig().getTokenSecret())
        .parseClaimsJws(token)
        .getBody();

    return claims.getId();
  }

  public boolean validateToken(String authToken)
      throws TokenInvalidException, TokenExpiredException {
    try {
      Jwts.parser().setSigningKey(applicationProperties.getAuthConfig().getTokenSecret())
          .parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
      throw new TokenInvalidException();
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
      throw new TokenExpiredException();
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");

    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }
}
