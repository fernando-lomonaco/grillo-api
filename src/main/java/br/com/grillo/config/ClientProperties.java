package br.com.grillo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "base.client.garde")
@Setter
@Getter
public class ClientProperties {

    private String url;
        
}
