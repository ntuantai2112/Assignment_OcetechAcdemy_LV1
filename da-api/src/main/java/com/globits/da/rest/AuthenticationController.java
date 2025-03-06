package com.globits.da.rest;

import com.globits.da.dto.request.AuthenticationRequest;
import com.globits.da.dto.request.IntrospectRequest;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.AuthenticationResponse;
import com.globits.da.dto.response.IntrospectResponse;
import com.globits.da.dto.response.UserResponse;
import com.globits.da.exception.CodeConfig;
import com.globits.da.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/user")
public class AuthenticationController {


    AuthenticationService service;

    @PostMapping("/generate-token")
    ApiResponse<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request) {

        AuthenticationResponse result = service.authenticated(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(CodeConfig.SUCCESS_CODE.getCode())
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> verifier(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {

        IntrospectResponse result = service.verifier(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/get-my-info")
    ApiResponse<UserResponse> getMyInfo() {
        System.out.println("Valid");
        UserResponse result = service.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

}
