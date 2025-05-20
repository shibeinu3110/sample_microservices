package com.micro.commonlib.common.exception;


import com.micro.commonlib.common.StandardResponse;
import com.micro.commonlib.common.suberror.ApiSubError;
import com.micro.commonlib.common.suberror.ApiValidatorError;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<StandardResponse<String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info("handleMethodArgumentNotValid");

        List<ApiSubError> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    Object rejectValue = ((FieldError) error).getRejectedValue();
                    String message = error.getDefaultMessage();

                    details.add(new ApiValidatorError(fieldName, rejectValue, message));
                });
        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_VALUE, details), HttpStatus.OK);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<StandardResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.info("handleMethodArgumentTypeMismatchException. Message = {}", ex.getMessage(), ex);
        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_FORMAT, ex.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<StandardResponse<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("handleMethodArgumentTypeMismatchException. Message = {}", ex.getMessage(), ex);
        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_VALUE, ex.getMessage()), HttpStatus.OK);
    }


    @ExceptionHandler(IOException.class)
    protected ResponseEntity<StandardResponse<String>> handleIOException(IOException ex) {
        log.info("handleIOException. Message = {}", ex.getMessage(), ex);
        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_FORMAT, ex.getMessage()), HttpStatus.OK);
    }



    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<StandardResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof DateTimeParseException && ex.getCause() instanceof IOException) {
            return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_FORMAT, ex.getMessage()), HttpStatus.OK);
        }
        log.info("handleHttpMessageNotReadable. Message = {}", ex.getMessage(), ex);
        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.INVALID_VALUE, ex.getMessage()), HttpStatus.OK);
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    protected ResponseEntity<StandardResponse<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
//        log.info("handleNoHandlerFoundException. Message = {}", ex.getMessage(), ex);
//        return new ResponseEntity<>(StandardResponse.build(ErrorMessages.NOT_FOUND), HttpStatus.OK);
//    }

    @ExceptionHandler(StandardException.class)
    protected ResponseEntity<StandardResponse<String>> handleOctException(StandardException ex) {
        log.info("handleOctException. Msg = {}", ex.getErrMsg().getMessage(), ex);
        return buildResponseEntity(ex);
    }

    private ResponseEntity<StandardResponse<String>> buildResponseEntity(StandardException ex) {
        return new ResponseEntity<>(StandardResponse.buildApplicationException(ex), HttpStatus.OK);
    }

}