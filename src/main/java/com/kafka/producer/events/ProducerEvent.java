package com.kafka.producer.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.producer.model.Library;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ProducerEvent {
//	Logger log = LoggerFactory.getLogger(ProducerEvent.class);
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	public void createNewLibrary(Library library) throws JsonProcessingException {
		Integer kafkaKey = library.getLibraryId();
		String kafkaValue = objectMapper.writeValueAsString(library);
		String topic = library.getTopic();
		
		ListenableFuture<SendResult<Integer, String>> listenableFuture = null;
		
		if(topic.isEmpty())
			listenableFuture = kafkaTemplate.sendDefault(kafkaKey, kafkaValue);
		else
			listenableFuture = kafkaTemplate.send(topic, kafkaKey, kafkaValue);
		
		listenableFuture.addCallback(successAndFailedMsg(kafkaKey, kafkaValue));
	}
	

	public ListenableFuture<SendResult<Integer,String>> sendLibraryEvent_Approach2(Library library) throws JsonProcessingException {

        Integer kafkaKey = library.getLibraryId();
        String kafkaValue = objectMapper.writeValueAsString(library);
        String topic = library.getTopic();

        ProducerRecord<Integer,String> producerRecord = buildProducerRecord(kafkaKey, kafkaValue, topic);

        ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(successAndFailedMsg(kafkaKey, kafkaValue));

        return listenableFuture;
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        List<Header> recordHeaders = new ArrayList<Header>();
        recordHeaders.add(new RecordHeader("event-source", "scanner".getBytes()));

        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }


    public SendResult<Integer, String> sendLibraryEventSynchronous(Library library) throws Exception {

        Integer key = library.getLibraryId();
        String value = objectMapper.writeValueAsString(library);
        SendResult<Integer,String> sendResult = null;
        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            log.error("ExecutionException/InterruptedException Sending the Message and the exception is {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Exception Sending the Message and the exception is {}", e.getMessage());
            throw e;
        }

        return sendResult;

    }
	
	private ListenableFutureCallback<SendResult<Integer, String>> successAndFailedMsg(Integer kafkaKey, String kafkaValue) {
		//handle Success Msg & Failed Msg
		return new ListenableFutureCallback<SendResult<Integer, String>>(){
			
			@Override
			public void onSuccess(@Nullable SendResult<Integer, String> result) {
				successMessage(kafkaKey, kafkaValue, result);
			}

			@Override
			public void onFailure(Throwable ex) {
				failedMessage(kafkaKey, kafkaValue, ex);				
			};
		};
	
	}
	
	
	private void successMessage(Integer key, String value, SendResult<Integer, String> result ) {
		log.info("Kafka Message Sent Successfully WITH | key: {} | value: {} | partitions: {}", key, value, result.getRecordMetadata().partition());
	}
	
	private void failedMessage(Integer key, String value, Throwable error ) {
		log.error("ERROR sending Kafka Message || {}", error.getMessage());
		try {
			throw error;
		}catch(Throwable e) {
			log.error("Error on FailedMessage {}", e.getMessage());
		}
	}
	
	
}
