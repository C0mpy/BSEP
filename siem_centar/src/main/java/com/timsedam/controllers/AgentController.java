package com.timsedam.controllers;

import com.timsedam.dto.ResponseDTO;
import com.timsedam.dto.UserDTO;
import com.timsedam.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/agent")
public class AgentController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

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

}
