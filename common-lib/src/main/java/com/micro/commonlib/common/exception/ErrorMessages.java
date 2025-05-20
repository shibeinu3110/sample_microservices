package com.micro.commonlib.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorMessages implements ErrorMessage {
    SUCCESS(200, "Success"),
    ACCESS_DENIED(403, "User is not allowed to access this resource"),
    BAD_REQUEST(400, "Bad request"),
    INVALID_VALUE(400_001, "Invalid value"),
    SAVE_DATABASE_ERROR(400_002, "Save database error"),
    NOT_FOUND(404, "Resource not found"),
    DUPLICATE(33,"Duplicate attribute"),
    INVALID_FORMAT(34,"invalid format input"),
    ;

    int code;
    String message;

}
