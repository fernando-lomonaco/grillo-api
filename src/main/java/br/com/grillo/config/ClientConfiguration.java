package br.com.grillo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
@RequiredArgsConstructor
public class ClientConfiguration {

    private ClientProperties clientProperties;
    
}
