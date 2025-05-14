package com.korutil.server.controller;

import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.user.record.EmailVerificationRequest;
import com.korutil.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/valid")
public class ValidController {

    private final UserService userService;

    @PostMapping("/email/send")
    public CommonApiResponse<String> sendEmail(@RequestParam String email) {
        return userService.putEmailVerification( email );
    }

    @PostMapping("/email/check")
    public CommonApiResponse<Boolean> isValidEmail(@RequestBody EmailVerificationRequest request) {
        return userService.isValidEmailVerification( request.toDto() );
    }

    @PostMapping("/username")
    public CommonApiResponse<Boolean> isNotValidUsernameAtSignup(@RequestParam String username ) {
        return userService.isNotValidUsernameVerification( username );
    }
}
