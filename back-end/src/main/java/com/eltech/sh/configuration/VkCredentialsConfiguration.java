package com.eltech.sh.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class VkCredentialsConfiguration {
    @Value("${vk.app_id}")
    private Integer appId;

    @Value("${vk.client_secret}")
    private String clientSecret;

    public Integer getAppId() {
        return appId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getOAuthUrl(){
        return "https://oauth.vk.com/authorize?client_id="
                + appId + "&display=page&redirect_uri="
                + getRedirectUri() + "&scope=groups&response_type=code";
    }

    public String getClientCredentialsUrl(){
        return "https://oauth.vk.com/access_token?client_id="
                + appId + "&client_secret="
                + clientSecret + "&v=5.68&grant_type=client_credentials";
    }

    public String getRedirectUri() {
        String host = "http://localhost:8080";
        return host + "/callback";
    }
}
