package org.example.expert.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public SignupResponse signup(
            @Valid @RequestPart("request") SignupRequest signupRequest,
            @RequestParam("image") MultipartFile image) {

        return authService.signup(signupRequest, image);
    }

    @PostMapping("/auth/signin")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }
}
