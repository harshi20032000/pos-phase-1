package com.hsw.pos_phase_1.service;

import com.hsw.pos_phase_1.entities.Otp;
import com.hsw.pos_phase_1.entities.User;
import com.hsw.pos_phase_1.models.UserLoginRequest;
import com.hsw.pos_phase_1.models.UserLoginResponse;
import com.hsw.pos_phase_1.repository.OtpRepository;
import com.hsw.pos_phase_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public UserLoginResponse loginWithPhone(UserLoginRequest request) {

        // Hardcoded OTP for now, to be replaced by a service.
        String otp = "123456";
        LocalDateTime now = LocalDateTime.now();

        // save new OTP
        Otp otpEntity = Otp.builder()
                .phoneNumber(request.getPhoneNumber())
                .otp(otp)
                .createdAt(now)
                .expiresAt(now.plusMinutes(5))
                .build();
        otpRepository.save(otpEntity);
        //Lets Encrypt this and store in db will redirect by ui from to enter otp from there and verify
        return new UserLoginResponse(request, true, "OTP Generated Proceed", null);
    }

    public UserLoginResponse verifyOTP(UserLoginRequest request) {
        Optional<Otp> otpOptional = otpRepository.findTopByPhoneNumberOrderByCreatedAtDesc(request.getPhoneNumber());

        if (otpOptional.isPresent()) {
            Otp otpEntity = otpOptional.get();

            if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
                return new UserLoginResponse(request, false, "OTP Expired", null);
            }

            if (otpEntity.getOtp().equals(request.getOtp())) {
                User user = getOrCreateUserByPhone(request.getPhoneNumber()); // clear OTP once used
                return new UserLoginResponse(request, true, "OTP Verified", maskUser(user));
            }
        }

        return new UserLoginResponse(request, false, "Invalid OTP", null);
    }

    public UserLoginResponse loginWithUserName(UserLoginRequest request) {

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            //User Present Redirect to verify Username with secret Answer using other service.
            User presetnUser = user.get();
            presetnUser.setSecretAnswer("xxxx");
            return new UserLoginResponse(request, true, "User Found, Proceed to Verify", presetnUser);
        }
        //User not found, redirect to create user with username
        return new UserLoginResponse(request, true, "User Not Found, Proceed to Create", null);
    }

    public User createUser(UserLoginRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .secretQuestion(request.getSecretQuestion())
                .secretAnswer(request.getSecretAnswer())
                .loyaltyNumber(0) // default
                .build();

        return userRepository.save(user);
    }

    public UserLoginResponse verifySecretAnswer(UserLoginRequest request) {
        //Username and answer will come in request.
        //using Username nno will retrieve answer from db.
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        User presetnUser = user.get();

        if (presetnUser.getSecretAnswer().equalsIgnoreCase(request.getSecretAnswer())) {
            presetnUser.setSecretAnswer("XXXX");
            return new UserLoginResponse(request, true, "Answer Matched", presetnUser);
        }

        return new UserLoginResponse(request, true, "Answer Didn't Match", null);
    }

    private User getOrCreateUserByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .phoneNumber(phoneNumber)
                            .username("guest_" + phoneNumber)
                            .loyaltyNumber(0)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    private User maskUser(User user) {
        user.setSecretAnswer("XXXX");
        return user;
    }


}
