package com.castillojuan.synchrony;

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
}
