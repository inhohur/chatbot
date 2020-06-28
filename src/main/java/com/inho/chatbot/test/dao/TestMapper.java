package com.inho.chatbot.test.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
	public List<?> selectTestList() throws Exception;
}
