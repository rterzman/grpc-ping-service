package com.grpc.ping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class GrpcPingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcPingServiceApplication.class, args);
	}

}
