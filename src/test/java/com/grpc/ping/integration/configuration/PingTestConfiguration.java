package com.grpc.ping.integration.configuration;

import com.grpc.ping.client.service.PingStreamObserverFactory;
import com.grpc.ping.server.model.URLPageInfo;
import com.grpc.ping.server.service.UrlConnectorService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@EnableConfigurationProperties
public class PingTestConfiguration {

    @Value("${test.ping.default-source}")
    private String defaultSource;

    @Bean
    @Primary
    public StreamObserver testResponseObserver() {
        return mock(StreamObserver.class);
    }


    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public PingStreamObserverFactory testObserverFactory(StreamObserver testResponseObserver) {
        final PingStreamObserverFactory observerFactory = mock(PingStreamObserverFactory.class);
        when(observerFactory.get(any())).thenReturn(testResponseObserver);

        return observerFactory;
    }


    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "test.instance", name = "mock", havingValue = "true")
    public UrlConnectorService testPingUrlService() {
        final UrlConnectorService urlConnectorService = mock(UrlConnectorService.class);
        final URLPageInfo testPageInfo = URLPageInfo.builder()
                .url(defaultSource)
                .responseTime(200)
                .responseText("{}")
                .build();

        when(urlConnectorService.getPageWithResponseTime(anyString())).thenReturn(testPageInfo);

        return urlConnectorService;
    }
}
