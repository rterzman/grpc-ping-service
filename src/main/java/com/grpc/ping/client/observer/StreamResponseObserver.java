package com.grpc.ping.client.observer;

import com.grpc.ping.proto.PingResponse;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class StreamResponseObserver implements StreamObserver<PingResponse> {
    private StreamResponseWrapper responseWrapper;

    public StreamResponseObserver() {
        responseWrapper = new StreamResponseWrapper();
    }

    @Override
    public void onNext(PingResponse response) {
        log.info("Response was received, source={}", response.getUrl());
        responseWrapper.addResponse(response);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Failed handling stream request, error={}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("Request was handled successfully");
    }


    public Map<String, PingResponse> getResponses() {
        return responseWrapper.getResponses();
    }
}
