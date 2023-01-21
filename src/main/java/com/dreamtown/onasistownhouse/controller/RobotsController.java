package com.dreamtown.onasistownhouse.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RobotsController {
    @RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
    public String getRobots(HttpServletRequest request) {
        return (Arrays.asList("https://onasistownhouse.com","onasistownhouse.com", "www.onasistownhouse.com").contains(request.getServerName())) ?
                "robotsAllowed" : "robotsDisallowed";
    }
}
