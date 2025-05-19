package com.micro.employeeservice.common.suberror;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiValidatorError implements ApiSubError {
    private String field;
    private transient Object rejectValue;
    private String message;
}
