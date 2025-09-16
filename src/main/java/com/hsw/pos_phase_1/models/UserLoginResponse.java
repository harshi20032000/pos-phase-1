package com.hsw.pos_phase_1.models;

import com.hsw.pos_phase_1.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponse {
    private UserLoginRequest request;
    private boolean success;
    private String message;
    private User user;
}
