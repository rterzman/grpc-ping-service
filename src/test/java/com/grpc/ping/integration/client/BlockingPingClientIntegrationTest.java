package com.grpc.ping.integration.client;

import com.grpc.ping.client.service.BlockingPingClient;
import com.grpc.ping.integration.GrpcPingServiceApplicationTests;
import com.grpc.ping.proto.PingResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BlockingPingClientIntegrationTest extends GrpcPingServiceApplicationTests {

    @Autowired
    private BlockingPingClient blockingPingClient;

    @Test
    public void shouldSendAndGetAllSourcesUnary() {
        pingSources
                .forEach( source -> {
                    final Optional<PingResponse> pingResponse = blockingPingClient.pingSource(source);
                    assertTrue(pingResponse.isPresent());

                    final PingResponse response = pingResponse.get();
                    assertTrue(response.getTs() >= 0);
                    assertNotNull(response.getPage());
                    assertTrue(pingSources.contains(response.getUrl()));
                });
    }

    @Test
    public void shouldSendAndGetAllSourcesMulti() {
        final List<PingResponse> responses = blockingPingClient.streamPingSource(pingSources);

        assertNotNull(responses);
        assertEquals(pingSources.size(), responses.size());
        checkResponses(responses);
    }
}
