package com.micro.employeeservice.common.suberror;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiMessageError implements ApiSubError{
    String errorMessage;

    public ApiMessageError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}