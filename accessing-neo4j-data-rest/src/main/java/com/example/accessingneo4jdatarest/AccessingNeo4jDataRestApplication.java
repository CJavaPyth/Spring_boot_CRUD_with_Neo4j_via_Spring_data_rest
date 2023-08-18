package com.example.accessingneo4jdatarest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.config.EnableNeo4jAuditing;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jAuditing
@EnableNeo4jRepositories(basePackages = {"com.example.accessingneo4jdatarest"})
public class AccessingNeo4jDataRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessingNeo4jDataRestApplication.class, args);
	}

}