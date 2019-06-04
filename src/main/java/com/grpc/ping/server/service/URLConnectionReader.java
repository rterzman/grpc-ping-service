package com.grpc.ping.server.service;

import com.grpc.ping.server.model.URLPageInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Component
public class URLConnectionReader {

    public URLPageInfo getPageWithResponseTime(String url) throws IOException {
        final URLConnectionWithResponseTime connectionResponse = getConnectionWithResponseTime(url);

        final HttpsURLConnection urlConnection = (HttpsURLConnection) connectionResponse.getUrlConnection();

        return URLPageInfo.builder()
                .pageSource(urlConnection.getResponseMessage())
                .build();
    }

    private URLConnectionWithResponseTime getConnectionWithResponseTime(String url) throws IOException {
        final URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("Content-Type", "text/html; charset=utf-8");

        return URLConnectionWithResponseTime.builder()
                .urlConnection(urlConnection)
                .build();
    }

    @Data
    @Builder(toBuilder = true)
    public static class URLConnectionWithResponseTime {
        private final URLConnection urlConnection;
        private final long responseTime;
    }
}
