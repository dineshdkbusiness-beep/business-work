package com.line.business.work.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.line.business.work.repository.UserRepository;
import com.line.business.work.request.dto.LoginResponse;
import com.line.business.work.response.dto.LoginRequest;
import com.line.business.work.security.JwtUtil;
import com.line.business.work.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

    	LoginResponse response = new LoginResponse();
    	 String username = request.getUserName();
    	 String password = request.getPassword();
    	 
    	 System.out.println("username --->"+username);
    	 System.out.println("password --->"+password);
        try {
        	
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        } catch (BadCredentialsException e) {
        	response.setCode("-1");
        	response.setStatus("Failed");
        	response.setMessage("Invalid UserName or Password");
        	 return ResponseEntity.ok(response);
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        String token = null;
        try {
        	token = jwtUtil.generateToken(user.getUsername());
        	user.setActiveToken(token);
        	userRepository.save(user);
        	
        } catch (RuntimeException e) {
        	response.setCode("-1");
        	response.setStatus("Failed");
        	response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        }
      
        
        JSONObject obj = new JSONObject();
        obj.put("UserDetails", user);
        obj.put("token", token);
        response.setCode("0");
    	response.setStatus("success");
    	response.setData(user);
    	response.setToken(token);
        return ResponseEntity.ok(response);
    }
}
