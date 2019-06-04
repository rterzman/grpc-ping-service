package com.grpc.ping.server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class URLPageInfo {
    private final long responseTime;
    private final String pageSource;
}
