package com.hsw.pos_phase_1.models;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;      // optional
    private String phoneNumber;
    private String otp;// optional
    private String secretAnswer;
    private String secretQuestion;
}

