package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dreamtown.onasistownhouse.utils.FileManager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private HttpSession httpSession; 

    @Autowired
    private FileManager fileManager; 

    @GetMapping(value = "/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping(value = "/ftp")
    public ResponseEntity<Map> ftp(Model model) {
        Map data = new HashMap<>();
        data.put("isConnected", fileManager.connected());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
