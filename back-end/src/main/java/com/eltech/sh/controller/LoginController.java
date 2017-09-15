package com.eltech.sh.controller;

import com.eltech.sh.service.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {

    private final VKService vkService;

    @Autowired
    public LoginController(VKService vkService) {
        this.vkService = vkService;
    }

    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = vkService.getAccessToken(request.getParameter("code"));
        request.getSession().setAttribute("access_token", accessToken);

        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
