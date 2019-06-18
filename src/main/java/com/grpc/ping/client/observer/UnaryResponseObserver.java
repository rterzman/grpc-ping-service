package com.grpc.ping.client.observer;

import com.grpc.ping.proto.PingResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnaryResponseObserver implements StreamObserver<PingResponse> {

    @Override
    public void onNext(PingResponse pingResponse) {
        log.info("Finished handling request with response={}", pingResponse);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Failed handle request with error {}", throwable.getCause());
    }

    @Override
    public void onCompleted() {
        log.info("Finished handle request");
    }
}
