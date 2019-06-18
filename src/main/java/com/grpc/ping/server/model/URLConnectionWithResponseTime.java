package com.grpc.ping.server.model;

import lombok.Builder;
import lombok.Data;

import java.net.URLConnection;

@Data
@Builder(toBuilder = true)
public class URLConnectionWithResponseTime {
    private final URLConnection urlConnection;
    private final long responseTime;
}
