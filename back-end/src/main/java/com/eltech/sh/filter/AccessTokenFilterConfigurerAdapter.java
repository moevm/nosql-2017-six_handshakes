package com.eltech.sh.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AccessTokenFilterConfigurerAdapter extends WebMvcConfigurerAdapter {

    private final AccessTokenFilter filter;

    @Autowired
    public AccessTokenFilterConfigurerAdapter(AccessTokenFilter filter) {
        this.filter = filter;
    }

    @Bean
    public FilterRegistrationBean accessTokenFilterRegistrationBean() {
        FilterRegistrationBean regBean = new FilterRegistrationBean();
        regBean.setFilter(filter);
        regBean.addUrlPatterns("/*");

        return regBean;
    }
}