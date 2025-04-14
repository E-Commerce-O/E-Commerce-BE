package org.example.cdweb_be.service;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    @NonFinal // đánh dấu để không tự tạo bở các annotation khác
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())// đại diện cho người dùng đang đăng nhập
                .issuer("quangHiu.com")// người cấp token
                .issueTime(new Date(Instant.now().toEpochMilli())) // thời gian phát hành token này
                .expirationTime(new Date(
                        Instant.now().plus(5, ChronoUnit.HOURS).toEpochMilli())) // thời gian hết hạn của token
                // thep nguyên tắc của oauth2 thì role có name là scope
                .claim("scope", user.getRole()) // thêm claim tự do
                .claim("id", user.getId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }

    }
    public long getUserId(String token){
        try {
            return getClaimsSet(token).getLongClaim("id");
        } catch (Exception e) {

            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }
    public JWTClaimsSet getClaimsSet(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token.substring(7));
            MACVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            if (jwsObject.verify(verifier)) {
                // Lấy claims từ token
                JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
                return claims;
            } else {
                throw new AppException(ErrorCode.ACCESS_TOKEN_INVALID);
            }

        } catch (Exception e) {
            throw new AppException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }
}
