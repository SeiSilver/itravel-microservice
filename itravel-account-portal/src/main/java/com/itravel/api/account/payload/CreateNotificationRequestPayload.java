package com.itravel.api.account.payload;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateNotificationRequestPayload {

  private Long receiverId;
  private String title;
  private String message;
  private ZonedDateTime createdAt;

}
