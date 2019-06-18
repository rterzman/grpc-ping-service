package com.grpc.ping.server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class URLPageInfo {
    private final String url;
    private final long responseTime;
    private final String responseText;
}
