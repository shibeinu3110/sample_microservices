package com.micro.employeeservice.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StandardException extends RuntimeException{
    final ErrorMessage errMsg;
    String detail;

    public StandardException(ErrorMessage errMsg) {
        super();
        this.errMsg = errMsg;
    }
    public StandardException(ErrorMessage errMsg, String detail) {
        super();
        this.errMsg = errMsg;
        this.detail = detail;
    }

    public StandardException() {
        super();
        errMsg = null;
    }
}