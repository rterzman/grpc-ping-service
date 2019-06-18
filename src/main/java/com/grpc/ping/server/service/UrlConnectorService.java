package com.grpc.ping.server.service;

import com.grpc.ping.server.model.URLPageInfo;

public interface UrlConnectorService {

    URLPageInfo getPageWithResponseTime(String source);


    default String normalizeUrl(String url) {
        if (url.startsWith("https") || url.startsWith("http")) {
            return url;
        }
        return "https://" + url;
    }
}
