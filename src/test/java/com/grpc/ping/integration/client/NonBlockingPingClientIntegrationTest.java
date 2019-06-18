package com.grpc.ping.integration.client;

import com.grpc.ping.client.service.NonBlockingPingClient;
import com.grpc.ping.integration.GrpcPingServiceApplicationTests;

import com.grpc.ping.proto.PingResponse;
import io.grpc.stub.StreamObserver;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class NonBlockingPingClientIntegrationTest extends GrpcPingServiceApplicationTests {

    @Autowired
    private NonBlockingPingClient client;

    @Autowired
    private StreamObserver testResponseObserver;

    @Captor
    private ArgumentCaptor<PingResponse> responseCaptor;

    @Test
    public void shouldSendUnaryRequest() {
        pingSources.forEach(
                source -> {
                    client.pingSource(source);

                    waitObserver(5, 1);

                    verify(testResponseObserver, atLeastOnce()).onNext(responseCaptor.capture());
                    verify(testResponseObserver, atLeastOnce()).onCompleted();

                    final PingResponse response = responseCaptor.getValue();

                    assertNotNull(response);
                    assertNotNull(response.getPage());
                    assertTrue(response.getTs() >= 0);
                }
        );
    }


    @Test
    public void shouldSendListRequests() {
        client.streamPingSource(pingSources);

        waitObserver(10, pingSources.size());

        verify(testResponseObserver, atLeastOnce()).onNext(responseCaptor.capture());
        verify(testResponseObserver, atLeastOnce()).onCompleted();

        final List<PingResponse> responses = responseCaptor.getAllValues();

        assertNotNull(responses);
        checkResponses(responses);
    }

    @Test
    public void shouldSendStreamRequest() {
        client.chatStreamPingSource(pingSources);

        waitObserver(10, pingSources.size());

        verify(testResponseObserver, atLeastOnce()).onNext(responseCaptor.capture());
        verify(testResponseObserver, atLeastOnce()).onCompleted();

        final List<PingResponse> responses = responseCaptor.getAllValues();

        assertNotNull(responses);
        checkResponses(responses);
    }


    private void waitObserver(int delay, int size) {
        await().atMost(30, TimeUnit.SECONDS).until(() ->
                Mockito.mockingDetails(testResponseObserver).getInvocations().size() >= size);
    }
}
