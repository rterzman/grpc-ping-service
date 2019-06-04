package com.grpc.ping;

import com.grpc.ping.server.service.URLConnectionReader;
import org.aspectj.lang.annotation.Around;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrpcPingServiceApplicationTests {

	@Autowired
	private URLConnectionReader connectionReader;


	@Test
	public void contextLoads() {
	}


	@Test
	public void test() throws IOException {
		connectionReader.getPageWithResponseTime("https://ggle.com");
	}

}
