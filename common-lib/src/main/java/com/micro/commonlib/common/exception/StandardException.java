package com.micro.commonlib.common.exception;

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
//        super();
        super(errMsg != null ? errMsg.getMessage() + (detail != null ? ": " + detail : "") : detail);
        this.errMsg = errMsg;
        this.detail = detail;
    }

    public StandardException() {
        super();
        errMsg = null;
    }
}