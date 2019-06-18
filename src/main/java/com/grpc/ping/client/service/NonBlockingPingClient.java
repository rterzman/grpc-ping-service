package com.grpc.ping.client.service;

import com.grpc.ping.proto.PingRequest;
import com.grpc.ping.proto.PingRequests;
import com.grpc.ping.proto.PingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.grpc.ping.client.service.PingStreamObserverFactory.ObserverType;

@Slf4j
public class NonBlockingPingClient {
    private final PingServiceGrpc.PingServiceStub serviceStub;
    private final PingStreamObserverFactory pingStreamObserverFactory;

    public NonBlockingPingClient(PingServiceGrpc.PingServiceStub serviceStub,
                                 PingStreamObserverFactory pingStreamObserverFactory) {
        this.serviceStub = serviceStub;
        this.pingStreamObserverFactory = pingStreamObserverFactory;
    }

    public void pingSource(String source) {
        log.info("Sending request, source={}", source);
        serviceStub.ping(PingRequest.newBuilder().setSource(source).build(),
                pingStreamObserverFactory.get(ObserverType.UNARY));
    }

    public void streamPingSource(List<String> sources) {
        log.info("Sending requests, sources=[{}]", sources);
        serviceStub.pingServerStream(PingRequests.newBuilder().addAllRequests(buildRequestList(sources)).build(),
                pingStreamObserverFactory.get(ObserverType.STREAM));
    }


    public void chatStreamPingSource(List<String> sources) {
        log.info("Sending requests, sources=[{}]", sources);
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
