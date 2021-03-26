package com.isd.internship.controller;

import com.isd.internship.entity.Role;
import com.isd.internship.entity.RoleName;
import com.isd.internship.entity.User;
import com.isd.internship.exceptions.AppException;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.JwtAuthenticationResponse;
import com.isd.internship.payload.LoginRequest;
import com.isd.internship.repository.RoleRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.security.JwtTokenProvider;
import com.isd.internship.service.UserService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import net.bytebuddy.utility.RandomString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(),loginRequest.getUsernameOrEmail()).orElseThrow(() -> new ResourceNotFoundException("Category", "Id", loginRequest.getUsernameOrEmail()));
        if(user.isEnabled()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        }else{
            Map<String,String> map = new HashMap<>();
            map.put("message","Email not verified.");
            return new ResponseEntity<Map<String,String>>(map,HttpStatus.UNAUTHORIZED);}
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(()->new AppException("User role was not set"));
        user.setRoles(Collections.singleton(role));
        String randomCode = RandomString.make(64);
        user.setCodeVerification(randomCode);
        user.setEnabled(false);
        User result = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        try {
            userService.sendVerificationEmail(user);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.created(location).body(new ApiResponse(true,"User registered successfully"));
    }
}
