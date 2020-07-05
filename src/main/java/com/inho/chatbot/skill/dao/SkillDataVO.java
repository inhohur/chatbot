package com.inho.chatbot.skill.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillDataVO {
	private String bot;
	private String skill;
	private String section;
	private int sequence;
	private String type;
	private String content;
}
