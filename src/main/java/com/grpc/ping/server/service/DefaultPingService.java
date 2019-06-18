package com.grpc.ping.server.service;

import com.grpc.ping.proto.PingRequest;
import com.grpc.ping.proto.PingRequests;
import com.grpc.ping.proto.PingResponse;
import com.grpc.ping.proto.PingServiceGrpc;
import com.grpc.ping.server.model.URLPageInfo;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import io.grpc.stub.StreamObservers;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GRpcService
public class DefaultPingService extends PingServiceGrpc.PingServiceImplBase {

    private final UrlConnectorService urlConnectorService;

    public DefaultPingService(UrlConnectorService urlConnectorService) {
        this.urlConnectorService = urlConnectorService;
    }

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        log.info("Handling request={}", request);
        final URLPageInfo pageInfo = urlConnectorService.getPageWithResponseTime(request.getSource());

        log.info("Request was handled successfully, request={}", request);
        responseObserver.onNext(buildResponse(pageInfo));
        responseObserver.onCompleted();
    }


    @Override
    public void pingServerStream(PingRequests requests,
                                 StreamObserver<PingResponse> responseObserver) {
        final List<PingResponse> responses = requests.getRequestsList().parallelStream()
                .map(request -> urlConnectorService.getPageWithResponseTime(request.getSource()))
                .map(this::buildResponse)
                .collect(Collectors.toList());

        final ServerCallStreamObserver callStreamObserver = (ServerCallStreamObserver) responseObserver;

        StreamObservers.copyWithFlowControl(responses.iterator(), callStreamObserver);
    }

    @Override
    public StreamObserver<PingRequest> pingBidiStream(StreamObserver<PingResponse> responseObserver) {
        return new StreamObserver<PingRequest>() {
            @Override
            public void onNext(PingRequest pingRequest) {
                log.info("Handling request, {}", pingRequest);
                responseObserver.onNext(buildResponse(urlConnectorService.getPageWithResponseTime(pingRequest.getSource())));
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Failed handling request, {}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Finished handling request");
                responseObserver.onCompleted();
            }
        };
    }


    private PingResponse buildResponse(URLPageInfo pageInfo) {
        return PingResponse.newBuilder()
                .setUrl(pageInfo.getUrl())
                .setTs(pageInfo.getResponseTime())
                .setPage(pageInfo.getResponseText())
                .build();
    }
}
