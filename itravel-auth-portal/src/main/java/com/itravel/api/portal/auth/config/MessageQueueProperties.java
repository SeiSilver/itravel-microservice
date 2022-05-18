package com.itravel.api.portal.auth.config;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application.messaging")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConstructorBinding
@Validated
public class MessageQueueProperties {

  @NotBlank
  private String itravelCreateNotification;

  @NotBlank
  private String itravelUpdateStatus;
  
  @NotNull
  @Valid
  private ListenerConfig listener;

  @Getter
  @RequiredArgsConstructor
  public static class ListenerConfig {

    @NotBlank
    private final String concurrency;

    @NotBlank
    private final String ackMode;
  }

}
