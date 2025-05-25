package com.micro.authservice.message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest implements Serializable {
    // mandatory
    String username;
    String password;

    // optional
    String platform; //access from web or mobile can use enum Platform {WEB, MOBILE}
    String deviceToken;
    String versionApp;
}
