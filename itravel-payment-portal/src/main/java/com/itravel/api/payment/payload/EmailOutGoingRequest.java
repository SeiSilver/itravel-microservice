package com.itravel.api.payment.payload;

import java.util.List;
import java.util.Map;
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
public class EmailOutGoingRequest {

  private String from;
  private String mailTo;
  private String subject;
  private List<Object> attachments;
  private Map<String, Object> props;

}
