package com.kafka.producer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.producer.events.ProducerEvent;
import com.kafka.producer.model.Library;
import com.sun.org.glassfish.gmbal.ParameterNames;

import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
public class LibraryController {
	@Autowired
	ProducerEvent producerEvent;
	
	@RequestMapping(value={"", "/", "/home"})
	public ResponseEntity<String> controllerCheck(){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("REST API - Library Controller");
	}
	
	@PostMapping(value= "/libraryEvent", headers = "content-type=application/json")
	public ResponseEntity<Library> postLibrary(@RequestBody Library library) throws JsonProcessingException{
		
		producerEvent.createNewLibrary(library);
		return ResponseEntity.status(HttpStatus.CREATED).body(library);
		//return new ResponseEntity<Library>(library, HttpStatus.ACCEPTED);
		
	}
	
}
