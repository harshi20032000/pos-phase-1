package com.hsw.pos_phase_1.controller;

import com.hsw.pos_phase_1.entities.User;
import com.hsw.pos_phase_1.models.BaseUIResponse;
import com.hsw.pos_phase_1.models.UserLoginRequest;
import com.hsw.pos_phase_1.models.UserLoginResponse;
import com.hsw.pos_phase_1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/loginWithPhone")
    public ResponseEntity<BaseUIResponse<UserLoginResponse>> loginWithPhone(@RequestBody UserLoginRequest request) {
        UserLoginResponse serviceResponse = userService.loginWithPhone(request);
        BaseUIResponse<UserLoginResponse> response = new BaseUIResponse<>();
        response.setResponsePayload(serviceResponse);
        response.setMessage(serviceResponse.getMessage());
        response.setStatus("SUCCESS");
        response.setCode("OTP_GENERATED");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<BaseUIResponse<UserLoginResponse>> verifyOtp(@RequestBody UserLoginRequest request) {
        UserLoginResponse serviceResponse = userService.verifyOTP(request);
        BaseUIResponse<UserLoginResponse> response = new BaseUIResponse<>();
        response.setResponsePayload(serviceResponse);
        response.setMessage(serviceResponse.getMessage());
        response.setStatus("SUCCESS");
        response.setCode("OTP_VERIFIED");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/loginWithUserName")
    public ResponseEntity<BaseUIResponse<UserLoginResponse>> loginWithUserName(@RequestBody UserLoginRequest request) {
        UserLoginResponse serviceResponse = userService.loginWithUserName(request);
        BaseUIResponse<UserLoginResponse> response = new BaseUIResponse<>();
        response.setResponsePayload(serviceResponse);
        response.setMessage(serviceResponse.getMessage());
        response.setStatus("SUCCESS");

        return ResponseEntity.ok(response);
    }

        @PostMapping("/createUser")
    public ResponseEntity<BaseUIResponse<User>> createUser(@RequestBody UserLoginRequest request) {
        User createdUser = userService.createUser(request);

        BaseUIResponse<User> response = new BaseUIResponse<>();
        response.setResponsePayload(createdUser);
        response.setMessage("User created successfully");
        response.setStatus("SUCCESS");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verifySecretAnswer")
    public ResponseEntity<BaseUIResponse<UserLoginResponse>> verifySecretAnswer(@RequestBody UserLoginRequest request) {
        UserLoginResponse serviceResponse = userService.verifySecretAnswer(request);
        BaseUIResponse<UserLoginResponse> response = new BaseUIResponse<>();
        response.setResponsePayload(serviceResponse);
        response.setMessage(serviceResponse.getMessage());
        response.setStatus("SUCCESS");

        return ResponseEntity.ok(response);
    }

}
