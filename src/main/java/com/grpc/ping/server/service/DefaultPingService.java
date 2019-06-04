package com.grpc.ping.server.service;

import com.grpc.ping.message.PingRequest;
import com.grpc.ping.message.PingResponse;
import com.grpc.ping.message.PingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class DefaultPingService extends PingServiceGrpc.PingServiceImplBase {

    @Override
    public void ping(PingRequest request,StreamObserver<PingResponse> responseObserver) {

    }

}
