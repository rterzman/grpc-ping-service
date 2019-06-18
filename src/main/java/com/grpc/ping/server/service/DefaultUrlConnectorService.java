package com.grpc.ping.server.service;

import com.grpc.ping.server.model.URLConnectionProperties;
import com.grpc.ping.server.model.URLConnectionWithResponseTime;
import com.grpc.ping.server.model.URLPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class DefaultUrlConnectorService implements UrlConnectorService {
    private static final String RESPONSE_DELIMITER = "\\A";

    private final URLConnectionProperties connectionProperties;

    public DefaultUrlConnectorService(URLConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    @Override
    public URLPageInfo getPageWithResponseTime(String source) {
        log.info("Handling input resource {}", source);
        final URLPageInfo.URLPageInfoBuilder pageInfoBuilder = URLPageInfo.builder().url(source);

        try {
            final URLConnectionWithResponseTime connectionResponse = getConnectionWithResponseTime(normalizeUrl(source));
            final HttpsURLConnection urlConnection = (HttpsURLConnection) connectionResponse.getUrlConnection();
            pageInfoBuilder.responseText(readResponse(urlConnection)).responseTime(connectionResponse.getResponseTime());
        } catch (Exception e) {
            log.error("Failed get data from input resource {}, {}", source, e.getLocalizedMessage());
            pageInfoBuilder.responseText(e.getLocalizedMessage());
        }

        return pageInfoBuilder.build();
    }

    private URLConnectionWithResponseTime getConnectionWithResponseTime(String url) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("Content-Type", connectionProperties.getContentType());

        stopWatch.stop();
        return URLConnectionWithResponseTime.builder()
                .urlConnection(urlConnection)
                .responseTime(stopWatch.getLastTaskTimeMillis())
                .build();
    }

    private String readResponse(HttpsURLConnection urlConnection) throws IOException {
        final InputStream responseStream = Optional.ofNullable(urlConnection.getErrorStream())
                .orElse(urlConnection.getInputStream());
        String response;
        try (Scanner scanner = new Scanner(responseStream)) {
            response = scanner.useDelimiter(RESPONSE_DELIMITER).next();
        }
        return response;
    }
}
