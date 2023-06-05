package com.kafka.producer.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerEvent {
	
//	@KafkaListener
	public void onMessage() {
		log.info("ConsumerEvent");
	}
	
	
}
