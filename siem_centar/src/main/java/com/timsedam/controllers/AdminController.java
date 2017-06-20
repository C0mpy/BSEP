package com.timsedam.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.timsedam.dto.ResponseDTO;
import com.timsedam.dto.UserDTO;
import com.timsedam.security.TokenUtils;
import com.timsedam.services.UserDetailsServiceImpl;
import com.timsedam.services.UserService;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = " application/json", produces = "application/json")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails details = userDetailsService.loadUserByUsername(userDTO.getEmail());
            ResponseEntity<ResponseDTO> resp = new ResponseEntity<ResponseDTO>(new ResponseDTO(tokenUtils.generateToken(details)), HttpStatus.OK);
            return resp;
        } catch (Exception ex) {
            return new ResponseEntity<ResponseDTO>(new ResponseDTO("login failed"), HttpStatus.UNAUTHORIZED);
        }

    }
    
    @RequestMapping(value = "/addAlarm", method = RequestMethod.POST, consumes = " application/json", produces = "application/json")
    public ResponseEntity addAlarm(@RequestBody ResponseDTO ruleDTO) {
        
    	
    	try {
    		String separator = File.separator;
    		Files.write(Paths.get("src" + separator + "main" + separator + "resources" 
    				+ separator + "drools.spring.rules" + separator + "rules.drl"), ruleDTO.getResponse().getBytes(), StandardOpenOption.APPEND);
            return new ResponseEntity<ResponseDTO>(new ResponseDTO("Alarm is succesfully added"), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        	return new ResponseEntity<ResponseDTO>(new ResponseDTO("Alarm isn't succesfully added"), HttpStatus.UNAUTHORIZED);
        }

    }
}
