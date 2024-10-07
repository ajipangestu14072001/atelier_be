package com.atelier.module.auth.service;

import com.atelier.module.auth.model.*;
import com.atelier.module.auth.model.request.LoginRequest;
import com.atelier.module.auth.model.request.RegisterRequest;
import com.atelier.module.user.model.request.ForgotPinRequest;
import com.atelier.module.user.repository.MRoleRepository;
import com.atelier.module.user.repository.MUserRepository;
import com.atelier.module.auth.repository.TUserAuthRepository;
import com.atelier.module.auth.repository.TUserSessionRepository;
import com.atelier.module.user.model.entity.MRole;
import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.auth.model.entity.TUserAuth;
import com.atelier.module.auth.model.entity.TUserSession;
import com.atelier.module.auth.model.response.LoginResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private MUserRepository mUserRepository;

    @Autowired
    private TUserAuthRepository tUserAuthRepository;

    @Autowired
    private TUserSessionRepository tUserSessionRepository;

    @Autowired
    private MRoleRepository mRoleRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TUserAuthRepository userAuthRepository;

    @Autowired
    private JavaMailSender mailSender;

    final Set<String> blacklistedTokens = new HashSet<>();


    @Transactional
    public void registerUser(RegisterRequest request) {
        Optional<MUser> existingUser = mUserRepository.findByEmailOrPhone(request.getEmail(), request.getPhoneNumber());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email or phone number already exists.");
        }

        if (!request.getTermCondition()) {
            throw new IllegalArgumentException("You must agree to the terms and conditions.");
        }

        String requestedRole = request.getRole() != null ? request.getRole().toLowerCase() : "user";
        Optional<MRole> roleOpt = mRoleRepository.findByRole(requestedRole);

        if (roleOpt.isEmpty()) {
            roleOpt = mRoleRepository.findByRole("user");
            if (roleOpt.isEmpty()) {
                throw new IllegalArgumentException("Default role 'user' does not exist.");
            }
        }
        MRole role = roleOpt.get();

        MUser newUser = new MUser();
        newUser.setPhone(request.getPhoneNumber());
        newUser.setEmail(request.getEmail());
        newUser.setInternalId(UUID.randomUUID().toString());
        newUser.setUsername(request.getUserName());
        newUser.setRole(role);
        newUser.setCreatedDate(LocalDateTime.now());
        mUserRepository.save(newUser);

        TUserAuth newUserAuth = new TUserAuth();
        newUserAuth.setUser(newUser);
        newUserAuth.setPassword(request.getPin());
        newUserAuth.setInternalId(UUID.randomUUID().toString());
        newUserAuth.setTermCondition(request.getTermCondition());
        newUserAuth.setCreatedDate(LocalDateTime.now());
        tUserAuthRepository.save(newUserAuth);

        createUserSession(newUser);
    }

    @Transactional
    public void logout(String token) {
        blacklistedTokens.add(token);
        String userId = tokenService.extractInternalId(token);
        Optional<MUser> userOptional = mUserRepository.findByInternalId(userId);
        if (userOptional.isPresent()) {
            MUser user = userOptional.get();
            Optional<TUserSession> userSessionOptional = tUserSessionRepository.findByUserAndIsActive(user);

            if (userSessionOptional.isPresent()) {
                TUserSession userSession = userSessionOptional.get();
                if (!userSession.getIsActive()) {
                    throw new IllegalStateException("User is already logged out.");
                }
                userSession.setIsActive(false);
                tUserSessionRepository.save(userSession);
            }
        }
    }

    @Transactional
    public LoginResponse loginUser(LoginRequest request) {
        if (request.getAuthenticationType() == null || request.getAuthenticationType().isEmpty()) {
            throw new IllegalArgumentException("Authentication type must be provided.");
        }

        TUserAuth userAuth = tUserAuthRepository.findByPassword(request.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credential."));

        MUser user = userAuth.getUser();

        switch (AuthenticationType.valueOf(request.getAuthenticationType().toUpperCase())) {
            case PASSWORD, PIN:
                if (!userAuth.getPassword().equals(request.getPassword())) {
                    throw new IllegalArgumentException("Invalid credential.");
                }
                break;

            case BIOMETRIC:
                if (!validateBiometric(userAuth.getBiometricTemplate(), request.getPassword())) {
                    throw new IllegalArgumentException("Invalid biometric data.");
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported authentication type.");
        }

        createUserSession(user);

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserID(user.getInternalId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setRole(getUserRoles(user));
        loginResponse.setToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpired(3600);
        loginResponse.setTokenType("Bearer");

        return loginResponse;
    }

    private List<String> getUserRoles(MUser user) {
        List<String> roles = new ArrayList<>();
        if (user.getRole() != null) {
            roles.add(user.getRole().getRole());
        }
        return roles;
    }

    private boolean validateBiometric(String biometricTemplate, String providedData) {
        return true;
    }

    private void createUserSession(MUser user) {
        TUserSession newUserSession = new TUserSession();
        newUserSession.setUser(user);
        newUserSession.setSessionId(UUID.randomUUID().toString());
        newUserSession.setIsActive(true);
        newUserSession.setLastActivity(LocalDateTime.now());
        newUserSession.setInternalId(UUID.randomUUID().toString());
        newUserSession.setCreatedDate(LocalDateTime.now());
        tUserSessionRepository.save(newUserSession);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUser user = mUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Optional<TUserAuth> userAuth = userAuthRepository.findByUser(user);
        if (userAuth.isEmpty()) {
            throw new UsernameNotFoundException("User authentication not found");
        }

        return new User(user.getInternalId(), userAuth.get().getPassword(), getAuthorities(user));
    }

    public UserDetails loadUserByPin(String pin) throws UsernameNotFoundException {
        Optional<TUserAuth> userAuth = userAuthRepository.findByPassword(pin);
        if (userAuth.isEmpty()) {
            throw new UsernameNotFoundException("PIN not found");
        }

        MUser user = userAuth.get().getUser();
        return new User(user.getInternalId(), userAuth.get().getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(MUser user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().getRole()));
    }

    public void forgotPin(ForgotPinRequest request) {
        Optional<MUser> userOptional = mUserRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            MUser user = userOptional.get();
            Optional<TUserAuth> userAuthOptional = tUserAuthRepository.findByUser(user);

            if (userAuthOptional.isPresent()) {
                String resetToken = UUID.randomUUID().toString();

                String resetLink = "http://yourdomain.com/reset-pin?token=" + resetToken;

                String subject = "Reset Your PIN";
                String content = "To reset your PIN, please click the link below:\n" + resetLink;
                sendEmail(request.getEmail(), subject, content);

                TokenStore.storeTokenForUser(userAuthOptional.get().getInternalId(), resetToken);
            }
        }
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
