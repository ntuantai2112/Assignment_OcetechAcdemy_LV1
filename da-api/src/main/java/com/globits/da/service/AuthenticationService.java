package com.globits.da.service;

import com.globits.da.dto.request.AuthenticationRequest;
import com.globits.da.dto.request.IntrospectRequest;
import com.globits.da.dto.response.AuthenticationResponse;
import com.globits.da.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {


    AuthenticationResponse authenticated(AuthenticationRequest request);

    IntrospectResponse verifier(IntrospectRequest request) throws JOSEException, ParseException;
}
