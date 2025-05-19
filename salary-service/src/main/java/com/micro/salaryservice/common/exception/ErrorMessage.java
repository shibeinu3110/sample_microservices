package com.micro.salaryservice.common.exception;

import java.io.Serializable;

public interface ErrorMessage extends Serializable {
    int getCode();
    String getMessage();
}
