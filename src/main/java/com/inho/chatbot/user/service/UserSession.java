package com.inho.chatbot.user.service;

import org.springframework.stereotype.Component;

@Component
public class UserSession {
	private static String userId = "JK@DJFKCKC";
	private static String userNickname = "김헬로";

	public String getUserId() {
		return userId;
	}
	
	public String getUserNickname() {
		return userNickname;
	}
}
