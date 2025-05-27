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
    ACCESS_DENIED(403, "User are not allowed to access this resource"),
    BAD_REQUEST(400, "Bad request"),
    INVALID_VALUE(400_001, "Invalid value"),
    SAVE_DATABASE_ERROR(400_002, "Save database error"),
    NOT_FOUND(404, "Resource not found"),
    DUPLICATE(33,"Duplicate attribute"),
    INVALID_FORMAT(34,"invalid format input"),
    INVALID_CREDENTIALS(401, "Invalid credentials"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    INVALID_TOKEN(401_001, "Invalid token"),
    EXPIRED_TOKEN(401_002, "Expired token"),
    INVALID_STATUS(401_003, "Invalid status"),
    ACCOUNT_DISABLED(401_004, "Account is disabled"),
    INTERNAL_ERROR(500_001, "Internal error"),
    ;

    int code;
    String message;

}
