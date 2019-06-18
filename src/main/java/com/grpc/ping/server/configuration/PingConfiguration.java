package com.grpc.ping.server.configuration;

import com.grpc.ping.server.model.URLConnectionProperties;
import com.grpc.ping.server.service.DefaultUrlConnectorService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PingConfiguration {

    @Bean
    @ConfigurationProperties("url.connection")
    public URLConnectionProperties urlConnectionProperties() {
        return new URLConnectionProperties();
    }

    @Bean
    public DefaultUrlConnectorService urlConnectionFacade(URLConnectionProperties urlConnectionProperties) {
        return new DefaultUrlConnectorService(urlConnectionProperties);
    }
}
