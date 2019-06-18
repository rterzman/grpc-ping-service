package com.grpc.ping.client.service;

import com.grpc.ping.proto.PingRequest;
import com.grpc.ping.proto.PingRequests;
import com.grpc.ping.proto.PingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.grpc.ping.client.service.PingStreamObserverFactory.ObserverType;

@AllArgsConstructor
public class NonBlockingPingClient {
    private final PingServiceGrpc.PingServiceStub serviceStub;
    private final PingStreamObserverFactory pingStreamObserverFactory;

    public void pingSource(String source) {
        serviceStub.ping(PingRequest.newBuilder().setSource(source).build(),
                pingStreamObserverFactory.get(ObserverType.UNARY));
    }

    public void streamPingSource(List<String> sources) {
        serviceStub.pingServerStream(PingRequests.newBuilder().addAllRequests(buildRequestList(sources)).build(),
                pingStreamObserverFactory.get(ObserverType.STREAM));
    }


    public void chatStreamPingSource(List<String> sources) {
        final StreamObserver<PingRequest> requestStreamObserver =
                serviceStub.pingBidiStream(pingStreamObserverFactory.get(ObserverType.STREAM));

        sources.forEach(
                source -> requestStreamObserver.onNext(buildRequest(source))
        );
    }

    private List<PingRequest> buildRequestList(List<String> sources) {
        return sources.stream()
                .map(this::buildRequest)
                .collect(Collectors.toList());
    }


    private PingRequest buildRequest(String source) {
        return PingRequest.newBuilder()
                .setSource(source)
                .build();
    }
}
