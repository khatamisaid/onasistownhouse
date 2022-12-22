package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.UserRepository;
import com.dreamtown.onasistownhouse.utils.Menu;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Menu menu;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("template", "index");
        model.addAttribute("menus", menu.getListProperty());
        return "admin/index";
    }

    @RequestMapping(value = "/managementUser", method = RequestMethod.GET)
    public String tambahUser(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("template", "managementUser");
        return "admin/index";
    }

    @RequestMapping(value = "/getUser/all", method = RequestMethod.GET)
    public ResponseEntity<Map> getUserAll(@RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length) {
        Pageable pageable = PageRequest.of(start, length, Sort.by("createdAt").descending());
        Role roleTemp = new Role();
        roleTemp.setIdRole(1);
        Page<User> pageUser = userRepository.findByRoleNot(roleTemp, pageable);
        Map res = new HashMap<>();
        res.put("data", pageUser);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
