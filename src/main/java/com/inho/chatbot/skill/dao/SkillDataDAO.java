package com.inho.chatbot.skill.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkillDataDAO {
	public List<SkillDataVO> selectSkillDataByBotSkillSection(Map<String,String> botSkillSection) throws Exception;

}
