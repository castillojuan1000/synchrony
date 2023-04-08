package com.castillojuan.synchrony.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImgurConfiguration {
    @Value("${imgur.clientId}")
    public String clientId;

    @Value("${imgur.clientSecret}")
    public String clientSecret;

    @Value("${imgur.apiUrl}")
    public String apiUrl;
    
    @Value("${imgur.auth}")
    public String auth;
}
