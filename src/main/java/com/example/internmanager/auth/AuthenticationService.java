package com.example.internmanager.auth;

import com.example.internmanager.config.JwtService;
import com.example.internmanager.model.Intern;
import com.example.internmanager.model.Mentor;
import com.example.internmanager.model.Role;
import com.example.internmanager.model.User;
import com.example.internmanager.repository.InternRepository;
import com.example.internmanager.repository.MentorRepository;
import com.example.internmanager.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository<User> userRepository;
    private final InternRepository internRepository;
    private final MentorRepository mentorRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository<User> userRepository, InternRepository internRepository, MentorRepository mentorRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.internRepository = internRepository;
        this.mentorRepository = mentorRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) throws IllegalArgumentException {
        User savedUser;

        if (request.getRole().equals(Role.INTERN)) {
            Intern intern = Intern.builder()
                    .fullName(request.getFullName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(request.getRole())
                    .dateOfBirth(request.getDateOfBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .position(request.getPosition())
                    .build();
            savedUser = internRepository.save(intern);
        } else if (request.getRole().equals(Role.MENTOR)) {
            Mentor mentor = Mentor.builder()
                    .fullName(request.getFullName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(request.getRole())
                    .dateOfBirth(request.getDateOfBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .position(request.getPosition())
                    .build();
            savedUser = mentorRepository.save(mentor);
        } else if (request.getRole().equals(Role.ADMIN)){
            savedUser = User.builder()
                    .fullName(request.getFullName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(request.getRole())
                    .dateOfBirth(request.getDateOfBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .position(request.getPosition())
                    .build();
            userRepository.save(savedUser);
        } else {
            throw new IllegalArgumentException("Invalid role provided");
        }

        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        User user = userRepository.findUsersByUsername(request.getUsername()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshTokenResponse refreshAccessToken(HttpServletRequest request) throws Exception {
        final String refreshToken;
        final String username;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingServletRequestParameterException("refreshToken", "String");
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            User user = userRepository.findUsersByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with the provided username."));
            if (jwtService.isRefreshTokenValid(refreshToken, user)) {
                refreshTokenResponse.setAccessToken(jwtService.generateAccessToken(user));
            } else {
                throw new Exception("Invalid refresh token.");
            }
        }

        return refreshTokenResponse;
    }
}
