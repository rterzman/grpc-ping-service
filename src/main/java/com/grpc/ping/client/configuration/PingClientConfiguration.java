package com.grpc.ping.client.configuration;

import com.grpc.ping.client.observer.UnaryResponseObserver;
import com.grpc.ping.client.service.BlockingPingClient;
import com.grpc.ping.client.service.NonBlockingPingClient;
import com.grpc.ping.client.service.PingStreamObserverFactory;

import com.grpc.ping.proto.PingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PingClientConfiguration {

    @Bean
    @ConfigurationProperties("managed.channel")
    public ManagedChannelProperties managedChannelProperties() {
        return new ManagedChannelProperties();
    }

    @Bean
    public ManagedChannel managedChannel(ManagedChannelProperties managedChannelProperties) {
        return ManagedChannelBuilder
                .forAddress(managedChannelProperties.getHost(), managedChannelProperties.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    public PingStreamObserverFactory pingStreamObserverFactory() {
        return new PingStreamObserverFactory();
    }

    @Bean
    public BlockingPingClient blockingClient(ManagedChannel managedChannel) {
        return new BlockingPingClient(PingServiceGrpc.newBlockingStub(managedChannel));
    }

    @Bean
    public StreamObserver responseObserver() {
        return new UnaryResponseObserver();
    }

    @Bean
    public NonBlockingPingClient nonBlockingClient(ManagedChannel managedChannel,
                                                   PingStreamObserverFactory pingStreamObserverFactory) {
        return new NonBlockingPingClient(PingServiceGrpc.newStub(managedChannel), pingStreamObserverFactory);
    }
}
