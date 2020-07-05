package com.inho.chatbot.global.exception;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public enum ExceptionCode {
	InvalidSkillOrBotWhenStartingSkill("InvalidSkillOrBotWhenStartingSkill", "봇이나 스킬 이름 두가지 중 하나가 잘 못 되어 있습니다."),
	InvalidBotName("InvalidBotName", "존재하지 않는 봇입니다."), 
	AlreadySkillInProgress("AlreadySkillInProgress", "이미 진행중인 스킬이 있습니다."), 
	NoSkillSessionInProgress("NoSkillSessionInProgress", "진행중인 스킬이 없습니다."), 
	DifferentSkillInProgress("DifferentSkillInProgress", "다른 스킬이 진행중입니다."), 
	NoSkillDataForTheChocie("NoSkillDataForTheChocie", "선택지에 대해 스킬데이터가 없습니다. 선택지 코드를 점검하세요.");

	public String code;
	public String description;

	private ExceptionCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
