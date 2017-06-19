package com.timsedam.controllers;


import com.timsedam.dto.MetricDTO;
import com.timsedam.dto.ResponseDTO;
import com.timsedam.dto.UserDTO;
import com.timsedam.models.Alarm;
import com.timsedam.models.Log;
import com.timsedam.models.Role;
import com.timsedam.models.User;
import com.timsedam.repository.RoleRepository;
import com.timsedam.security.TokenUtils;
import com.timsedam.services.AlarmService;
import com.timsedam.services.LogService;
import com.timsedam.services.UserDetailsServiceImpl;
import com.timsedam.services.UserService;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/operator")
public class OperatorController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private KieSession kieSession;

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

        kieSession.insert(new User(userDTO.getEmail(), userDTO.getPassword(), new Role("OPERATOR")));
        kieSession.fireAllRules();
        System.out.println(kieSession.getFactCount());

        return new ResponseEntity<ResponseDTO>(new ResponseDTO(tokenUtils.generateToken(details)), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/getLogsByMonitor/{monitor_id}",
            method = RequestMethod.GET,
            produces= "application/json")
    public ResponseEntity get(@PathVariable String monitor_id) {

        List<Log> l=logService.findAllByMonitorId(monitor_id);
        return new ResponseEntity<>(l,HttpStatus.OK);

    }
    @RequestMapping(value = "/getAllAgents", method = RequestMethod.GET)
    public ResponseEntity getAllAgents() {

        List<String> agents = logService.findAllAgents();
        return new ResponseEntity(agents, HttpStatus.OK);
    }

    @RequestMapping(value = "/getMonitors/{agentId}", method = RequestMethod.GET)
    public ResponseEntity getMonitors(@PathVariable String agentId) {

        List<String> a = logService.findMonitors(agentId);
        return new ResponseEntity(a, HttpStatus.OK);
    }

    @RequestMapping(
            value="/getLogNumber",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="text/plain"
    )
    public ResponseEntity getLogNum(@RequestBody MetricDTO metricDTO){


        String log_num=logService.getLogNum(
                metricDTO.getAgent(),metricDTO.getMonitor(),
                metricDTO.getStart(),metricDTO.getEnd()
        );
        System.out.println(log_num);

        return new ResponseEntity(log_num,HttpStatus.OK);
    }

    @RequestMapping(
            value="/getAlarmNumber",
            method = RequestMethod.POST,
            consumes="application/json",
            produces="text/plain"
    )
    public ResponseEntity getAlarmNum(@RequestBody MetricDTO metricDTO){


        String log_num=alarmService.getAlarmNum(
                metricDTO.getAgent(),metricDTO.getMonitor(),
                metricDTO.getStart(),metricDTO.getEnd()
        );
        System.out.println(log_num);

        return new ResponseEntity(log_num,HttpStatus.OK);
    }

    @RequestMapping(
            value="/getAlarms/{agentId}/{monitorId}",
            method = RequestMethod.GET,
            produces="application/json"
    )
    public ResponseEntity getAlarmNum(@PathVariable String agentId,@PathVariable String monitorId){

       List<Alarm> alarmi;
       if(agentId.equals("all")) {alarmi=alarmService.findAll();}
       else if(!monitorId.equals("all")){alarmi=alarmService.findAllByMonitorId(monitorId); }
       else{alarmi= alarmService.findAllByAgentIdAndMonitorId(agentId,monitorId);}

        return new ResponseEntity(alarmi,HttpStatus.OK);
    }


}
