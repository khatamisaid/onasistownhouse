package com.dreamtown.onasistownhouse.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dreamtown.onasistownhouse.utils.Menu;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    
    @Autowired
    private HttpSession httpSession;

    @Autowired 
    private Menu menu;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("template", "index");
        return "admin/index";
    }

    @RequestMapping(value = "/managementUser", method = RequestMethod.GET)
    public String tambahUser(Model model){
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("template", "managementUser");
        return "admin/index";
    }
}
