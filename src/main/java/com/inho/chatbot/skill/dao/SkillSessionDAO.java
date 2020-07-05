package com.inho.chatbot.skill.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SkillSessionDAO {
	
	public SkillSessionVO selectSkillSessionByUserBot(Map<String,String> param) throws Exception;

	public void insertSkillSessionByUserBotSkillSection(Map<String, String> param) throws Exception;

	public void updateSectionOfSkillSessionByUserBotSkill(SkillSessionVO skillSessionVO) throws Exception;

	public void deleteSkillSessionByUserBot(Map<String, String> param) throws Exception;

}

