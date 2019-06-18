package com.grpc.ping.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class URLConnectionProperties {
    private String contentType;
    private int connectionTimeOut;
    private int readTimeOut;
}
