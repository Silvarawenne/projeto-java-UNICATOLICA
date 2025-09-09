package com.example.demo.resources.exceptions;

import java.io.Serializable;


public class StandardError implements Serializable{
	
	private static final long serialVerisionUID = 1L;
	
	private Long timestamnp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	public StandardError() {
		super();
	}
	
	
	public StandardError(Long timestamp, Integer status, String error, String message, String path) {
		super();
		this.setTimestamnp(timestamp);
		this.setStatus(status);
		this.setError(error);
		this.setMessage(message);
		this.setPath(path);
		
		
		
		
	}


	public Long getTimestamnp() {
		return timestamnp;
	}


	public void setTimestamnp(Long timestamnp) {
		this.timestamnp = timestamnp;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
