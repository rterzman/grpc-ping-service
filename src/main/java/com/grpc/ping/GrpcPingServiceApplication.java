package com.grpc.ping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcPingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcPingServiceApplication.class, args);
	}

}
