package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtown.onasistownhouse.utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private Environment env;

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

    @PostMapping(value = "/upload")
    public ResponseEntity<Map> upload(@RequestParam("file") MultipartFile multipartFile) {
        Map data = new HashMap<>();
        try {
            multipartFile.transferTo(new File(env.getProperty("storage") + multipartFile.getOriginalFilename()));
            data.put("message", "sukses upload file");
        } catch (IllegalStateException | IOException e) {
            System.out.println(e.getMessage());
            data.put("message", "gagal upload file");
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
