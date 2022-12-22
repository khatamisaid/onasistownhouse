package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/property")
public class PropertyController {
    

    public ResponseEntity<Map> postProperty(){
        Map response = new HashMap<>();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
