package com.grpc.ping.client.observer;


import com.grpc.ping.proto.PingResponse;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class StreamResponseWrapper {
    private final Map<String, PingResponse> responses;

    public StreamResponseWrapper() {
        responses = new ConcurrentHashMap<>();
    }

    public void addResponse(PingResponse response) {
        responses.putIfAbsent(response.getUrl(), response);
    }
}
