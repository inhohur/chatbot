package com.inho.chatbot.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@RestControllerAdvice
public class AppExceptionAdvice {

	@ExceptionHandler(AppException.class)
	public Object appExceptionHandler(AppException e, HandlerMethod handlerMethod) {
		Logger logger = LoggerFactory.getLogger(handlerMethod.getClass());

		String logMessage = e.getMessage() + " " + handlerMethod.getClass().getName() + " : " + handlerMethod.getMethod().getName();
		logger.error(logMessage);

		Map<String, String> result = new HashMap<String, String>();
		
		result.put("result", "appexception");
		result.put("code", String.valueOf(e.getCode()));
		result.put("description", e.getMessage());
		return result;
	}
	
	@ExceptionHandler(Exception.class)
	public Object etcExceptionHandler(Exception e, HandlerMethod handlerMethod) {
		Logger logger = LoggerFactory.getLogger(handlerMethod.getClass());

		String logMessage = e.getMessage() + " " + handlerMethod.getClass().getName() + " : " + handlerMethod.getMethod().getName();
		logger.error(logMessage, e);

		Map<String, String> result = new HashMap<String, String>();
		
		result.put("result", "exception");
		result.put("class", e.getClass().toString());
		return result;
	}
}
