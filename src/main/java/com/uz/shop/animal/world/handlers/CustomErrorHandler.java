package com.uz.shop.animal.world.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.utils.Translator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorHandler extends ResponseEntityExceptionHandler {

    private static ObjectNode objectNode = new ObjectMapper().createObjectNode();
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request){
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errors.add(
                    Translator.translate(violation.getPropertyPath().toString()) + ": " + violation.getMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Object> handleRestClientResponseException(RestClientResponseException exception, WebRequest request) {
        ArrayNode arrayNode = objectNode.putArray("errors");
        arrayNode.add(exception.getMessage());
        objectNode.put("Status", exception.getStatusText());
        objectNode.put("message", "Error");
        return ResponseEntity.status(exception.getRawStatusCode()).body(objectNode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> errorList = ex
                .getBindingResult()
                .getFieldErrors();

        ArrayNode arrayNode = objectNode.putArray("errors");
        for(FieldError error : errorList) {
            arrayNode.add(
                    Translator.translate(error.getField()) + " : " +  error.getDefaultMessage()
            );
        }
        objectNode.put("Status", HttpStatus.BAD_REQUEST.name());
        objectNode.put("message", "Error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
    }

}