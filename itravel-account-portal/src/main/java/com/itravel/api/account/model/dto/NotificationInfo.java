package com.itravel.api.account.model.dto;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationInfo {

  private Integer id;

  private Integer receiverId;

  private String title;

  private String message;

  private Boolean hasSeen;

  private ZonedDateTime createdAt;

}
