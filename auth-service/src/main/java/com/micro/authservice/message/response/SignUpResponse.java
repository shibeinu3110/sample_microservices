package com.micro.authservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpResponse {
    private String token;
    private String name;
    private String roleId;
    private String roleName;
}
