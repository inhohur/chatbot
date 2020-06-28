package com.inho.chatbot.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inho.chatbot.test.dao.TestDAO;
import com.inho.chatbot.test.dao.TestMapper;

@Service
public class TestService {
	
	@Autowired
	private TestMapper testMapper;
	
	public List<?> selectTestList() throws Exception {
		return testMapper.selectTestList();
	}

}
