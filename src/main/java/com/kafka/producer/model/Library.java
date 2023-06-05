package com.kafka.producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//@Setter
//@Getter
public class Library {
	@JsonProperty(value="libraryId")
	private Integer libraryId;
	@JsonProperty(value="book")
	private Book book;
	
	@JsonProperty(value="topic")
	private String topic;
	
	
//	public Integer getLibraryId() {
//		return libraryId;
//	}
//	public void setLibraryId(Integer libraryId) {
//		this.libraryId = libraryId;
//	}
//	public Book getBook() {
//		return book;
//	}
//	public void setBook(Book book) {
//		this.book = book;
//	}
//	public String getTopic() {
//		return topic;
//	}
//	public void setTopic(String topic) {
//		this.topic = topic;
//	}
	
	

}
