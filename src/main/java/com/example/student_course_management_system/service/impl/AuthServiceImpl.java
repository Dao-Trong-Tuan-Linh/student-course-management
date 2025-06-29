package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.model.Role;
import com.example.student_course_management_system.model.User;
import com.example.student_course_management_system.model.UserRole;
import com.example.student_course_management_system.repository.RoleRepository;
import com.example.student_course_management_system.repository.UserRepository;
import com.example.student_course_management_system.request.auth.LoginRequest;
import com.example.student_course_management_system.request.auth.SignUpRequest;
import com.example.student_course_management_system.response.JwtAuthenticationResponse.JwtAuthenticationResponse;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.exception.ApiException;
import com.example.student_course_management_system.response.exception.AppException;
import com.example.student_course_management_system.response.exception.BadRequestException;
import com.example.student_course_management_system.response.exception.UnAuthorizedRequestException;
import com.example.student_course_management_system.security.JwtTokenProvider;
import com.example.student_course_management_system.service.AuthService;
import com.example.student_course_management_system.service.CustomUserDetailsService;
import com.example.student_course_management_system.utils.Message;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public ResponseEntity<ApiResponse> signupService(SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, Message.USERNAME_EXISTS.getMessage());
            throw new BadRequestException(apiResponse);
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, Message.EMAIL_EXISTS.getMessage());
            throw new BadRequestException(apiResponse);
        }

        String username = signUpRequest.getUsername();
        String email = signUpRequest.getEmail();
        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(username, email, password);

        List<Role> roles = new ArrayList<>();

        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(UserRole.STUDENT).orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, Message.USER_ROLE_NOT_SET.getMessage())));
            roles.add(roleRepository.findByName(UserRole.ADMIN).orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, Message.USER_ROLE_NOT_SET.getMessage())));
        } else if(signUpRequest.getRoles() != null && signUpRequest.getRoles().length > 0) {
            for(UserRole userRole : signUpRequest.getRoles()) {
                Role role = roleRepository.findByName(userRole).orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, Message.USER_ROLE_NOT_SET.getMessage()));
                roles.add(role);
            }
        } else {
                roles.add(roleRepository.findByName(UserRole.STUDENT).orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, Message.USER_ROLE_NOT_SET.getMessage())));
            }

        user.setRoles(roles);
        User createdUser = userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Tài khoản đã được đăng ký thành công"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiDataResponse> loginService(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            ResponseCookie cookie = ResponseCookie.from("refresh-token",refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofMillis(jwtTokenProvider.getRefreshTokenExpiration()))
                    .sameSite("Strict")
                    .build();

            ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"Đã xác thực thành công");
            ApiDataResponse response = new ApiDataResponse(apiResponse,accessToken);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString()).body(response);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, Message.ACCOUNT_NOT_RIGHT.getMessage());
            throw new BadRequestException(apiResponse);
        }
    }

    @Override
    public ResponseEntity<JwtAuthenticationResponse> refreshService(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("refresh-token".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();

                    if(jwtTokenProvider.validateRefreshToken(refreshToken)) {
                        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);
                        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,null,userDetails.getAuthorities()
                        );
                        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                        return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken));
                    }
                }
            }
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, Message.UNAUTHORIZED.getMessage());
        throw new UnAuthorizedRequestException(apiResponse);
    }
}
