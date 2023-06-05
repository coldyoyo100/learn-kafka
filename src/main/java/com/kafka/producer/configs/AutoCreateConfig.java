package com.kafka.producer.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import lombok.extern.slf4j.Slf4j;

@Configuration
//@Profile("local")
@Slf4j
public class AutoCreateConfig {

	@Bean
	public NewTopic newLibrary() {
		return TopicBuilder
				.name("New-Library")
				.partitions(3)
				.replicas(1)
				.build();
	}
}
