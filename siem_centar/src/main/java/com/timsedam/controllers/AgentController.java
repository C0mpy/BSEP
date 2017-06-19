package com.timsedam.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kie.api.runtime.KieSession;
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

import com.timsedam.dto.LogDTO;
import com.timsedam.dto.UserDTO;
import com.timsedam.models.Log;
import com.timsedam.security.TokenUtils;
import com.timsedam.services.AlarmService;
import com.timsedam.services.LogService;

@RestController
@RequestMapping(value="/api/agent")
public class AgentController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogService logService;
    
    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;
    
    @Autowired
    private KieSession kieSession;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = " application/json", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails details = userDetailsService.loadUserByUsername(userDTO.getEmail());
            ResponseEntity<String> resp = new ResponseEntity<String>(tokenUtils.generateToken(details), HttpStatus.OK);
            return resp;
        } catch (Exception ex) {
            return new ResponseEntity<String>("login failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/savelog", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity saveLog(@RequestBody LogDTO logDTO) {

        try {
            Log log = new Log();
            log.setAgentId(logDTO.getAgentId());
            log.setLog(logDTO.getLog());
            log.setMonitorId(logDTO.getMonitorId());
            log.setRegex(logDTO.getRegex());
            log.setStructure(logDTO.getStructure());
            log.setSystem(logDTO.getSystem());
            log.setLogName(logDTO.getLogName());
            log.setType(logDTO.getType());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
            Date d = sdf.parse(logDTO.getDate());
            log.setDate(d);
            logService.save(log);
            
            log.parseLogData();
            kieSession.insert(log);
            kieSession.fireAllRules();
            
            return new ResponseEntity<>("Log is saved in database", HttpStatus.OK);
        }catch (Exception e){
        	e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
