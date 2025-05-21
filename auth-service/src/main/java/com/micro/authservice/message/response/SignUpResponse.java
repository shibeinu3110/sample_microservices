package com.micro.authservice.message.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
    private String token;
    private String name;
    private String roleId;
    private String roleName;
}
