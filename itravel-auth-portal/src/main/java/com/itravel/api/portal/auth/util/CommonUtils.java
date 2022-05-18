package com.itravel.api.portal.auth.util;

import com.itravel.api.portal.auth.constraint.ErrorCode;
import com.itravel.api.portal.auth.constraint.ErrorMessage;
import com.itravel.api.portal.auth.constraint.ValidationRegexpPattern;
import com.itravel.api.portal.auth.exception.ApplicationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

  public static ZonedDateTime convertUTCDate(Date date) {

    ZonedDateTime dateFm = null;
    if (date != null) {
      dateFm = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("GMT+07"));
    }

    return dateFm;
  }

  public static ZonedDateTime convertUTCDate(LocalDateTime date) {
    return date.atZone(ZoneId.of("GMT+07"));
  }

  public static void checkFormatEmail(String email) throws ApplicationException {
    if (!isEmail(email)) {
      throw new ApplicationException(ErrorCode.INCORRECT_EMAIL_FORMAT, ErrorMessage.INCORRECT_EMAIL_FORMAT);
    }
  }

  private static boolean isEmail(String email) {
    return email.matches(ValidationRegexpPattern.EMAIL);
  }

}
