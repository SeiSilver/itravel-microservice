package com.itravel.api.shop;

import com.itravel.api.shop.config.ApplicationProperties;
import com.itravel.api.shop.util.CommonUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableDiscoveryClient
@EnableConfigurationProperties(ApplicationProperties.class)
@Log4j2
public class ShopPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopPortalApplication.class, args);
	}

	@Bean
	public DateTimeProvider auditingDateTimeProvider() {
		return () -> Optional.of(CommonUtils.convertUTCDate(LocalDateTime.now()));
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public DateTimeFormatter formatter(){
		return DateTimeFormatter.ofPattern("yyyy-MM-dd");
	}


	@Bean
	public RabbitListenerErrorHandler processRequestErrorHandler() {
		return (amqpMessage, message, exception) -> {
			log.error("processNewRequestErrorHandler", exception);

			return null;
		};
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
