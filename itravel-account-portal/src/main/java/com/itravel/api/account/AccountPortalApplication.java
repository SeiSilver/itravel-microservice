package com.itravel.api.account;

import com.itravel.api.account.util.CommonUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@Log4j2
public class AccountPortalApplication {

  public static void main(String[] args) {
    SpringApplication.run(AccountPortalApplication.class, args);
  }

  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(CommonUtils.convertUTCDate(LocalDateTime.now()));
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @Bean
  public RabbitListenerErrorHandler processRequestErrorHandler() {
    return (amqpMessage, message, exception) -> {
      log.error("processNewRequestErrorHandler", exception);
      return null;
    };
  }

}
