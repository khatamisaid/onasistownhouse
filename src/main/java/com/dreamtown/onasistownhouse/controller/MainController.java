package com.dreamtown.onasistownhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private HttpSession httpSession; 

    @GetMapping(value = "/")
    public String index(Model model) {
        return "index";
    }
}
