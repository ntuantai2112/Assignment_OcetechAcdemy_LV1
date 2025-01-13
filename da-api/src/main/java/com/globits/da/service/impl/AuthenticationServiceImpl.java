package com.globits.da.service.impl;

import com.globits.da.domain.UserEntity;
import com.globits.da.dto.request.AuthenticationRequest;
import com.globits.da.dto.request.IntrospectRequest;
import com.globits.da.dto.response.AuthenticationResponse;
import com.globits.da.dto.response.IntrospectResponse;
import com.globits.da.exception.AppException;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.repository.UserEntityRepository;
import com.globits.da.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    UserEntityRepository userEntityRepository;

    PasswordEncoder passwordEncoder;


    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticated(AuthenticationRequest request) {

        UserEntity user = userEntityRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCodeException.USER_NOT_FOUND)
        );

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            log.error("Cannot generate token");
            throw new AppException(ErrorCodeException.UNAUTHENTICATED);
        }

        String token = generateToke(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();

    }

    @Override
    public IntrospectResponse verifier(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean authenticated = signedJWT.verify(verifier) && expiryTime.after(new Date());
        return IntrospectResponse.builder()
                .valid(authenticated)
                .build();
    }


    private String generateToke(UserEntity user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("Oceantech.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token:", e);
            throw new RuntimeException(e);
        }


    }


    private String buildScope(UserEntity user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(s -> stringJoiner.add(s.getRoleName()));
        }
        return stringJoiner.toString();
    }


}
