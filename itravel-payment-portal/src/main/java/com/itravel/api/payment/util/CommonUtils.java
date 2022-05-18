package com.itravel.api.payment.util;

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

  public static Date convertToDate(ZonedDateTime date) {
    return Date.from(date.toInstant());
  }

  public static ZonedDateTime convertUTCDate(LocalDateTime date) {
    return date.atZone(ZoneId.of("GMT+07"));
  }

  public static double convertVNDToUSD(int amount) {
    return amount * 0.000044;
  }

}
