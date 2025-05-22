package org.example.cdweb_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.request.*;
import org.example.cdweb_be.dto.response.LoginResponse;
import org.example.cdweb_be.dto.response.UserResponse;
import org.example.cdweb_be.entity.OTP;
import org.example.cdweb_be.entity.RefreshToken;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.enums.Role;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.UserMapper;
import org.example.cdweb_be.respository.OtpRepository;
import org.example.cdweb_be.respository.RefreshTokenRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    AuthenticationService authenticationService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RefreshTokenRepository refreshTokenRepository;
    EmailService emailService;
    OtpRepository otpRepository;
    ObjectMapper objectMapper;
    String DEFAULT_IMAGE_PATH = "https://i.imgur.com/W60xqJf.png";
    public UserResponse addUser(UserCreateRequest request){
        Optional<User> userOptional = null;
//        validEmail(request.getEmail());
        userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) throw new AppException(ErrorCode.USERNAME_EXISTED);
        userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) throw new AppException(ErrorCode.EMAIL_EXISTED);
        userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent()) throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        User user = userMapper.toUser(request);
        user.setRole(Role.USER.name());
        user.setAvtPath(DEFAULT_IMAGE_PATH);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public UserResponse updateUser(String token, UserUpdateRequest request){
        String username = authenticationService.getClaimsSet(token).getSubject();
        User user = userRepository.findByUsername(username).get();
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent() && !user.getEmail().equals(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent() && !user.getPhoneNumber().equals(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAvtPath(request.getAvtPath());
        user.setGender(request.getGender());
        user.setFullName(request.getFullName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return userMapper.toUserResponse(userRepository.save(user));

    }
    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }
        String accessToken = authenticationService.generateToken(user);
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUserId(user.getId());
        if(refreshTokenOptional.isPresent())
            refreshTokenRepository.delete(refreshTokenOptional.get());
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .userId(user.getId())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .expriredAt(new Timestamp(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli()))
                .build();
        refreshTokenRepository.save(refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        try {

            RefreshToken refreshToken = refreshTokenRepository.findById(request.getRefreshToken())
                    .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
            User user = userRepository.findById(refreshToken.getUserId()).get();
            String accessToken = authenticationService.generateToken(user);
            refreshToken.setExpriredAt(new Timestamp(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli()));
            refreshTokenRepository.save(refreshToken);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

        }catch (Exception e){
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }
    public boolean validEmail(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})*\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        log.info("Valid email: "+matcher.matches());
        if(!matcher.matches()){
            return false;
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) return false;
        return true;
    }
    public UserResponse getMyInfo(String token){
        try{
            long userId = authenticationService.getClaimsSet(token).getLongClaim("id");
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                return userMapper.toUserResponse(userOptional.get());
            } else {
                throw new AppException(ErrorCode.USER_NOT_EXISTS);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }
//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserResponse> result = users.stream().map(user ->
                userMapper.toUserResponse(user)
        ).collect(Collectors.toList());
        return result;

    }
    public String validToken(ValidTokenRequest accessToken){
        JWTClaimsSet claimsSet = authenticationService.getClaimsSet("Bearer "+accessToken.getAccessToken());
        Date expireAt =  claimsSet.getExpirationTime();
        if (expireAt.before(new Date(System.currentTimeMillis()))){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }else{

            return "AccessToken is still valid";
        }

    }
    @Transactional
    public String sendOTP(String userNameOrEmail){
        Optional<User> userOptional = userRepository.findByEmail(userNameOrEmail);
        if(userOptional.isEmpty()) userOptional = userRepository.findByUsername(userNameOrEmail);
        if(userOptional.isEmpty()) throw new AppException(ErrorCode.USERNAME_OR_EMAIL_NOT_EXISTS);
        User user = userOptional.get();
        String otp = "";
        Random rd = new Random();
        while(otp.length() <6){
            otp += rd.nextInt(10);
        }

        otpRepository.deleteByEmail(user.getEmail());
        OTP otpEntity = OTP.builder()
                .otp(otp)
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .expireAt(new Timestamp(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli()))
                .build();
        boolean success = emailService.sendEmailResetPassword(otpEntity);
        if(!success){
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
        otpEntity = otpRepository.save(otpEntity);

        return "OTP has been sent to email "+otpEntity.getEmail();
    }
    public JsonNode verifyOTP(VerifyOtpRequest request){
        Optional<User> userOptional = userRepository.findByEmail(request.getUsernameOrEmail());
        if(userOptional.isEmpty()) userOptional = userRepository.findByUsername(request.getUsernameOrEmail());
        if(userOptional.isEmpty()) throw new AppException(ErrorCode.USERNAME_OR_EMAIL_NOT_EXISTS);

        Optional<OTP> otpOptional = otpRepository.findByEmail(request.getUsernameOrEmail());
        if(otpOptional.isEmpty()) otpOptional = otpRepository.findByUsername(request.getUsernameOrEmail());
        if(otpOptional.isEmpty()) throw new AppException(ErrorCode.USERNAME_OR_EMAIL_INVALID_OTP);

        OTP otp = otpOptional.get();
        if(otp.getVerified() != null && !otp.getVerified().isEmpty() ) throw new AppException(ErrorCode.OTP_VERIFIED);
        if(otp.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) throw new AppException(ErrorCode.OTP_EXPIRED);
        if(!otp.getOtp().equals(request.getOtp())) throw new AppException(ErrorCode.OTP_INCORRECT);
        String verified = UUID.randomUUID().toString();
        while (otpRepository.findByVerified(verified).isPresent()){
            verified = UUID.randomUUID().toString();
        }
        otp.setVerified(verified);
        otpRepository.save(otp);
        JsonNode response = objectMapper.createObjectNode()
                .put("resetPasswordToken", verified);
        return response;
    }
    public String resetPassword(ResetPasswordRequest request){
        OTP otp = otpRepository.findByVerified(request.getResetPasswordToken()).orElseThrow(() ->
                new AppException(ErrorCode.RESET_TOKEN_INVALID));
        if(request.getNewPassword().length()<6) throw new AppException(ErrorCode.PASSWORD_INVALID);
        Optional<User> userOptional = userRepository.findByUsername(otp.getUsername());
        if(userOptional.isEmpty()) userOptional = userRepository.findByEmail(otp.getEmail());
        if(userOptional.isEmpty()) throw new AppException(ErrorCode.SERVER_ERROR);
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        otpRepository.delete(otp);
        return "Reset pasword successful";

    }
}
