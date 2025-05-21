package com.micro.authservice.message.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    // mandatory
    private String username;
    private String password;

    // optional
    private String platform; //access from web or mobile can use enum Platform {WEB, MOBILE}
    private String deviceToken;
    private String versionApp;
}
