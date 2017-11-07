package com.eltech.sh.filter;

import com.eltech.sh.configuration.VkCredentialsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AccessTokenFilter extends GenericFilterBean {

    private final VkCredentialsConfiguration configuration;

    @Autowired
    public AccessTokenFilter(VkCredentialsConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        if (session.getAttribute("access_token") == null) {
            httpServletResponse.sendRedirect(configuration.getOAuthUrl());
        } else {
            chain.doFilter(request, response);
        }

    }
}
