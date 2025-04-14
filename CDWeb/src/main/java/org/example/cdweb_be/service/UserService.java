package org.example.cdweb_be.service;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cdweb_be.dto.request.*;
import org.example.cdweb_be.dto.response.LoginResponse;
import org.example.cdweb_be.dto.response.UserResponse;
import org.example.cdweb_be.entity.RefreshToken;
import org.example.cdweb_be.entity.User;
import org.example.cdweb_be.enums.Role;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.mapper.UserMapper;
import org.example.cdweb_be.respository.RefreshTokenRepository;
import org.example.cdweb_be.respository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    AuthenticationService authenticationService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RefreshTokenRepository refreshTokenRepository;
    String DEFAULT_IMAGE_PATH = "https://i.imgur.com/W60xqJf.png";
    public UserResponse addUser(UserCreateRequest request){
        Optional<User> userOptional = null;
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

}
