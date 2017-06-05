package com.timsedam.controllers;


import com.timsedam.dto.UserDTO;
import com.timsedam.models.User;
import com.timsedam.services.LogService;
import com.timsedam.services.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/operator")
public class OperatorController {

    @Autowired
    private OperatorService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> saveLogs(@RequestBody UserDTO userDTO) {

        User user = new User();
        System.out.println(userDTO);
        return new ResponseEntity<String>("Login successful", HttpStatus.OK);
    }
}
