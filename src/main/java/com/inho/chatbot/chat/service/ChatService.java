package com.inho.chatbot.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inho.chatbot.global.exception.AppException;
import com.inho.chatbot.global.exception.ExceptionCode;
import com.inho.chatbot.skill.dao.SkillSessionVO;
import com.inho.chatbot.skill.service.SkillSessionService;
import com.inho.chatbot.user.service.UserSession;

@Service
public class ChatService {
	@Autowired
	private UserSession userSession;
	
	@Autowired
	private SkillSessionService skillSessionService;

	public String userInput(String bot, String text) throws Exception {
		
		// 현재 진행중인 스킬이 있는 경우 일반 채팅을 진행할 수 없다.
		skillSessionService.checkThereIsNoSkillInProgress(bot);
		
		
		// 봇 별 대화 로직으로 연결.
		switch(bot) {
			case "LAMAMA":
				return text + " 이라구? 난 라마마야~";
			default :
				throw new AppException(ExceptionCode.InvalidBotName);
		}
	}

}
