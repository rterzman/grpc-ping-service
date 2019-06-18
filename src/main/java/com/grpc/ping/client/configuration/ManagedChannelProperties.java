package com.grpc.ping.client.configuration;

import lombok.Data;

@Data
public class ManagedChannelProperties {
    private String host;
    private int port;
}
