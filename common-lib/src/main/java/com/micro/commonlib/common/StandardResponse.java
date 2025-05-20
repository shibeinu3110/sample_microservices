package com.micro.commonlib.common;

import com.fasterxml.jackson.annotation.JsonFormat;


import com.micro.commonlib.common.exception.ErrorMessage;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.common.suberror.ApiSubError;
import com.micro.commonlib.utils.DateHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class StandardResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.GLOBAL_DATE_TIME)
    final LocalDateTime timestamp = LocalDateTime.now();
    int code;
    String message;
    List<ApiSubError> details;
    T data;
    int total;

    public static <T> StandardResponse<T> build(ErrorMessage errorMessage) {
        StandardResponse<T> response = new StandardResponse<>();
        response.code = errorMessage.getCode();
        response.message = errorMessage.getMessage();
        return response;
    }

    public static <T> StandardResponse<T> build(ErrorMessage errorMessage, List<ApiSubError> details) {
        StandardResponse<T> response = build(errorMessage);
        response.details = details;
        return response;
    }

    public static <T> StandardResponse<T> build(T data) {
        StandardResponse<T> response = new StandardResponse<>();
        response.data = data;
        if (data instanceof Collection) {
            response.total = ((Collection<?>) data).size();
        }
        response.code = 200;
        return response;
    }

    public static <T> StandardResponse<T> build(T data, String message) {
        StandardResponse<T> response = new StandardResponse<>();
        response.data = data;
        if (data instanceof Collection) {
            response.total = ((Collection<?>) data).size();
        }
        response.code = 200;
        response.message = message;
        return response;
    }

    public static <T> StandardResponse<T> build(T data, Integer total) {
        StandardResponse<T> response = build(data);
        response.total = total;
        return response;
    }

    public static <T> StandardResponse<T> build(T data, ErrorMessage errorMessage) {
        StandardResponse<T> response = build(errorMessage);
        response.data = data;
//        if (data instanceof Collection) {
//            response.total = ((Collection<?>) data).size();
//        }
//        response.code = errorMessage.getCode();
        return response;
    }

    public static <T> StandardResponse<T> build(ErrorMessage errorMessage, String message) {
        StandardResponse<T> response = build(errorMessage);
        response.message = message;
        return response;
    }


    public static <T> StandardResponse<T> build(String message, Integer errCode) {
        StandardResponse<T> response = new StandardResponse<>();
        response.code = errCode;
        response.message = message;
        return response;
    }


    public static StandardResponse<String> buildApplicationException(StandardException exception) {
        return build(exception.getDetail(), exception.getErrMsg());
    }
}
