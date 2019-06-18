package com.grpc.ping.client.service;

import com.grpc.ping.client.observer.StreamResponseObserver;
import com.grpc.ping.client.observer.UnaryResponseObserver;
import com.grpc.ping.proto.PingResponse;
import io.grpc.stub.StreamObserver;

public class PingStreamObserverFactory {

    public StreamObserver<PingResponse> get(ObserverType type) {
        switch (type) {
            case UNARY:  return new UnaryResponseObserver();
            case STREAM: return new StreamResponseObserver();
            default: break;
        }

        throw new IllegalStateException(String.format("Unknown type %s", type));
    }

    public enum ObserverType {
        UNARY, STREAM
    }
}
