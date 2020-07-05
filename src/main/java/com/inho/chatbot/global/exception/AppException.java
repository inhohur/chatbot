package com.inho.chatbot.global.exception;


public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ExceptionCode exceptionCode;
	
	public AppException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode; 
	}
	
	@Override
	public String getMessage() {
		return exceptionCode.description;
	}
	
	public String getCode() {
		return exceptionCode.code;
	}
}
