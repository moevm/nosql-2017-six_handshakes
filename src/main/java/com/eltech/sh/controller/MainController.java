package com.eltech.sh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {
    @GetMapping("/")
    public String who(HttpServletRequest request){
        return "Access Token: " + request.getSession().getAttribute("access_token");
    }
}
