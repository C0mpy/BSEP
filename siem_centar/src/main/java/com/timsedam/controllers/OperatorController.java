package com.timsedam.controllers;


import com.timsedam.dto.ResponseDTO;
import com.timsedam.dto.UserDTO;
import com.timsedam.models.Role;
import com.timsedam.models.User;
import com.timsedam.repository.RoleRepository;
import com.timsedam.security.TokenUtils;
import com.timsedam.services.OperatorService;
import com.timsedam.services.UserDetailsServiceImpl;
import com.timsedam.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/operator")
public class OperatorController {

    @Autowired
    private OperatorService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity login(@RequestBody UserDTO userDTO) {
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken token = null;
        try {
            token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
            authentication = authenticationManager.authenticate(token);

        } catch (Exception ex) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User u = new User();
            Role r = roleRepository.findByName("OPERATOR");

            u.setEmail(userDTO.getEmail());
            u.setPassword(encoder.encode(userDTO.getPassword()));
            u.setRole(r);

            userService.save(u);

            token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
            authentication = authenticationManager.authenticate(token);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails details = userDetailsService.loadUserByUsername(userDTO.getEmail());
        return new ResponseEntity<ResponseDTO>(new ResponseDTO(tokenUtils.generateToken(details)), HttpStatus.OK);
    }


}
