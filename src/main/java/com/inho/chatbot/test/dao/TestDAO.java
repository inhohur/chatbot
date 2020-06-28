package com.inho.chatbot.test.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestDAO {
	private String name;
	private String id;
	private String pw;
}
