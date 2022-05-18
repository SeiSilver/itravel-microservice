package com.itravel.api.shop.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AwsConfig {

  @Value("${archive.accessKey}")
  private String accessKey;

  @Value("${archive.secretKey}")
  private String secretKey;


  private static final String REGION = "ap-southeast-1";

  @Bean
  public AmazonS3 amazonS3() {
    AWSCredentials awsCredentials =
        new BasicAWSCredentials(accessKey, secretKey);
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(REGION)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }

}
