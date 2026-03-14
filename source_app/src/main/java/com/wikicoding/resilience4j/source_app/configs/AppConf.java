package com.wikicoding.resilience4j.source_app.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class AppConf {
    private final JokesRetryListener jokesRetryListener;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
