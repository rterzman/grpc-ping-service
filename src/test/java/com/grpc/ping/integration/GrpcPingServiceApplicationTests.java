package com.grpc.ping.integration;

import com.grpc.ping.GrpcPingServiceApplication;
import com.grpc.ping.integration.configuration.PingTestConfiguration;
import com.grpc.ping.proto.PingResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GrpcPingServiceApplication.class, PingTestConfiguration.class})
public abstract class GrpcPingServiceApplicationTests {

    @Value("${test.ping.sources}")
    protected List<String> pingSources;

    @Test
    public void contextLoads() {
    }

    protected void checkResponses(List<PingResponse> responses) {
        responses.forEach(
                response -> {
                    assertTrue(pingSources.contains(response.getUrl()));
                    assertNotNull(response);
                    assertNotNull(response.getPage());
                    assertTrue(response.getTs() >=0);
                }
        );
    }
}
