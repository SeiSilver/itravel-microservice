package com.itravel.api.payment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itravel.api.payment.exception.InvalidJsonException;
import com.itravel.api.payment.exception.MissingRequireFieldException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayloadUtils {

  private static final String REQUIRED_FIELD_ERROR_MESSAGE = "Missing required fields: ";

  private final ObjectMapper objectMapper;
  private final Validator validator;

  public <T> T parseJson(String data, Class<T> clazz) throws InvalidJsonException {
    try {
      return objectMapper.readValue(data, clazz);
    } catch (JsonProcessingException e) {
      throw new InvalidJsonException(e);
    }
  }

  public <T> T parseJson(String data, TypeReference<T> typeReference) throws InvalidJsonException {
    try {
      return objectMapper.readValue(data, typeReference);
    } catch (JsonProcessingException e) {
      log.error("Can not parse JSON data", e);
      throw new InvalidJsonException(e);
    }
  }

  public String parseObjectToString(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public <T> void validateRequiredFields(T requestPayload) throws MissingRequireFieldException {
    Set<ConstraintViolation<T>> violations = validator.validate(requestPayload);
    if (!violations.isEmpty()) {
      log.error("invalid payload with violations " + violations.stream()
          .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
          .collect(Collectors.toList()));
      List<String> listErrorMessage = violations.stream()
          .map(violation -> violation.getPropertyPath().toString())
          .collect(Collectors.toList());
      String errorMessage = REQUIRED_FIELD_ERROR_MESSAGE + String.join(", ", listErrorMessage);

      throw new MissingRequireFieldException(errorMessage);
    }
  }

}
