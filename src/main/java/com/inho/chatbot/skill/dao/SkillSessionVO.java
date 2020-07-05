package com.inho.chatbot.skill.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillSessionVO {
	private String userId;
	private String bot;
	private String skill;
	private String currentSection;
	private String sessionVariables;
	
}
