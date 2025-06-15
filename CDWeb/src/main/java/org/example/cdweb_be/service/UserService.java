package org.example.cdweb_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.request.*;
import org.example.cdweb_be.dto.response.LoginResponse;
import org.example.cdweb_be.dto.response.PagingResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    MessageProvider messageProvider;
    String DEFAULT_IMAGE_PATH = "https://i.imgur.com/W60xqJf.png";
    public UserResponse addUser(UserCreateRequest request){
        
        Optional<User> userOptional = null;
//        validEmail(request.getEmail());
        userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) throw new AppException(messageProvider,ErrorCode.USERNAME_EXISTED);
        userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) throw new AppException(messageProvider,ErrorCode.EMAIL_EXISTED);
        userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent()) throw new AppException(messageProvider,ErrorCode.PHONE_NUMBER_EXISTED);
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
            throw new AppException(messageProvider,ErrorCode.EMAIL_EXISTED);
        userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent() && !user.getPhoneNumber().equals(request.getPhoneNumber()))
            throw new AppException(messageProvider,ErrorCode.PHONE_NUMBER_EXISTED);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setAvtPath(request.getAvtPath());
        user.setGender(request.getGender());
        user.setFullName(request.getFullName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return userMapper.toUserResponse(userRepository.save(user));

    }
    public String changePassword(String token, ChangePasswordRequest request){
        String username = authenticationService.getClaimsSet(token).getSubject();
        User user = userRepository.findByUsername(username).get();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AppException(messageProvider, ErrorCode.PASSWORD_INCORRECT);
        if(!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException(messageProvider, ErrorCode.CONFIRM_PASSWORD_INCORRECT);
        if(request.getOldPassword().equals(request.getNewPassword()))
            throw new AppException(messageProvider, ErrorCode.PASSWORD_NO_CHANGE);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        return messageProvider.getMessage("user.update.password");

    }
    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(messageProvider,ErrorCode.USER_NOT_EXISTS));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(messageProvider,ErrorCode.PASSWORD_INCORRECT);
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
                    .orElseThrow(() -> new AppException(messageProvider,ErrorCode.REFRESH_TOKEN_NOT_FOUND));
            User user = userRepository.findById(refreshToken.getUserId()).get();
            String accessToken = authenticationService.generateToken(user);
            refreshToken.setExpriredAt(new Timestamp(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli()));
            refreshTokenRepository.save(refreshToken);
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

        }catch (Exception e){
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }
    public String validEmail(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})*\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        log.info("Valid email: "+matcher.matches());
        if(!matcher.matches()){
            throw new AppException(messageProvider, ErrorCode.EMAIL_INVALID);
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) throw new AppException(messageProvider, ErrorCode.EMAIL_EXISTED);
        return messageProvider.getMessage("email.valid");
    }
    public UserResponse getMyInfo(String token){
        try{
            long userId = authenticationService.getClaimsSet(token).getLongClaim("id");
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                return userMapper.toUserResponse(userOptional.get());
            } else {
                throw new AppException(messageProvider,ErrorCode.USER_NOT_EXISTS);
            }
        } catch (Exception e) {
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }
//    @PreAuthorize("hasRole('ADMIN')")
    public PagingResponse  getAllUsers(int page, int size, String search, String role){
        Pageable pageable = PageRequest.of(page-1, size);
        Page<User> users ;
        long totalItem;
        if(!search.isEmpty() && !role.isEmpty()){
            users = userRepository.findAll(pageable, search, role);
            totalItem = userRepository.countAll(search, role);
        }else if(!search.isEmpty()){
            users = userRepository.findAllByName(pageable, search);
            totalItem = userRepository.countAllByName(search);
        }else if(!role.isEmpty()){
            users = userRepository.findAllByRole(pageable, role);
            totalItem = userRepository.countAllByRole(role);
        }else{
            users = userRepository.findAll(pageable);
            totalItem = userRepository.count();
        }
        List<UserResponse> userResponses = users.stream().map(user ->
                userMapper.toUserResponse(user)
        ).collect(Collectors.toList());
        return PagingResponse.<UserResponse>builder()
                .page(page)
                .size(size)
                .totalItem(totalItem)
                .data(userResponses)
                .build();

    }

    public String validToken(ValidTokenRequest accessToken){
        JWTClaimsSet claimsSet = authenticationService.getClaimsSet("Bearer "+accessToken.getAccessToken());
        Date expireAt =  claimsSet.getExpirationTime();
        if (expireAt.before(new Date(System.currentTimeMillis()))){
            throw new AppException(messageProvider,ErrorCode.TOKEN_EXPIRED);
        }else{

            return messageProvider.getMessage("valid.token");
        }

    }
    @Transactional
    public String sendOTP(String userNameOrEmail){
        Optional<User> userOptional = userRepository.findByEmail(userNameOrEmail);
        if(userOptional.isEmpty()) userOptional = userRepository.findByUsername(userNameOrEmail);
        if(userOptional.isEmpty()) throw new AppException(messageProvider,ErrorCode.USERNAME_OR_EMAIL_NOT_EXISTS);
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
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
        otpEntity = otpRepository.save(otpEntity);

        return messageProvider.getMessage("send.otp")+otpEntity.getEmail();
    }
    public JsonNode verifyOTP(VerifyOtpRequest request){
        Optional<User> userOptional = userRepository.findByEmail(request.getUsernameOrEmail());
        if(userOptional.isEmpty()) userOptional = userRepository.findByUsername(request.getUsernameOrEmail());
        if(userOptional.isEmpty()) throw new AppException(messageProvider,ErrorCode.USERNAME_OR_EMAIL_NOT_EXISTS);

        Optional<OTP> otpOptional = otpRepository.findByEmail(request.getUsernameOrEmail());
        if(otpOptional.isEmpty()) otpOptional = otpRepository.findByUsername(request.getUsernameOrEmail());
        if(otpOptional.isEmpty()) throw new AppException(messageProvider,ErrorCode.USERNAME_OR_EMAIL_INVALID_OTP);

        OTP otp = otpOptional.get();
        if(otp.getVerified() != null && !otp.getVerified().isEmpty() ) throw new AppException(messageProvider,ErrorCode.OTP_VERIFIED);
        if(otp.getExpireAt().before(new Timestamp(System.currentTimeMillis()))) throw new AppException(messageProvider,ErrorCode.OTP_EXPIRED);
        if(!otp.getOtp().equals(request.getOtp())) throw new AppException(messageProvider,ErrorCode.OTP_INCORRECT);
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
                new AppException(messageProvider,ErrorCode.RESET_TOKEN_INVALID));
        if(request.getNewPassword().length()<6) throw new AppException(messageProvider,ErrorCode.PASSWORD_INVALID);
        Optional<User> userOptional = userRepository.findByUsername(otp.getUsername());
        if(userOptional.isEmpty()) userOptional = userRepository.findByEmail(otp.getEmail());
        if(userOptional.isEmpty()) throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        otpRepository.delete(otp);
        return messageProvider.getMessage("reset.password");

    }
    public UserResponse getById(long userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new AppException(messageProvider, ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserResponse(user);
    }
    public String setRole(long userId, String role){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new AppException(messageProvider, ErrorCode.USER_NOT_EXISTS));
        role = Strings.toRootUpperCase(role);
        log.info("role: "+role);
        try {
            Role  enumRole = Role.valueOf(role);
        } catch (Exception e) {
            throw new AppException(messageProvider, ErrorCode.ROLE_INVALID);
        }
            if (user.getRole().equals(Role.ADMIN)) throw new AppException(messageProvider, ErrorCode.CANT_CHANGE_ADMIN_ROLE);
            if(role.equalsIgnoreCase(user.getRole())) {throw new AppException(messageProvider, ErrorCode.ROLE_NO_HAVE_CHANGE);}
            user.setRole(role);
            userRepository.save(user);
            return messageProvider.getMessage("user.role.change");

    }
}
