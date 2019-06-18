package com.grpc.ping.client.service;


import com.grpc.ping.proto.PingRequest;
import com.grpc.ping.proto.PingRequests;
import com.grpc.ping.proto.PingResponse;
import com.grpc.ping.proto.PingServiceGrpc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class BlockingPingClient {
    private final PingServiceGrpc.PingServiceBlockingStub blockingServiceStub;

    public BlockingPingClient(PingServiceGrpc.PingServiceBlockingStub blockingServiceStub) {
        this.blockingServiceStub = blockingServiceStub;
    }


    public Optional<PingResponse> pingSource(String source) {
        log.info("Sending request, source={}", source);
        return Optional.ofNullable(blockingServiceStub.ping(PingRequest.newBuilder().setSource(source).build()));
    }

    public List<PingResponse> streamPingSource(List<String> sources) {
        log.info("Sending requests, sources=[{}]", sources);
        final PingRequests.Builder requestsBuilder = PingRequests.newBuilder();
        sources.forEach(source -> requestsBuilder.addRequests(PingRequest.newBuilder().setSource(source).build()));

        final Iterator<PingResponse> pingResponseIterator = blockingServiceStub.pingServerStream(requestsBuilder.build());
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(pingResponseIterator, Spliterator.ORDERED),
                false)
                .collect(Collectors.toList());
    }
}
