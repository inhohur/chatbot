package com.inho.chatbot.chat.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inho.chatbot.chat.service.ChatService;
import com.inho.chatbot.skill.service.SkillSessionService;

@RestController
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private SkillSessionService skillSessionService;
	
	@Autowired
	private ChatService chatService;
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * 스킬이 시작되지 않았을 때 자유대화 모드일 때 이 API가 호출된다.
	 * 아직 라마마는 말이 익숙하지 못해 유저가 한 말을 따라하기만 한다.
	 */
	@PostMapping("/user")
	public Map<String, String> userChat(@RequestParam String bot, @RequestParam String text) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		String reply = chatService.userInput(bot, text);
		result.put("botchat", reply);

		return result;
	}

	/*
	 * 채팅방에서 스킬을 선택하면 이 API를 호출하게 된다.
	 * 세션정보를 확인해서 현재 진행중인 스킬이 있으면 "진행중인 대화가 있어" 라고 익셉션을 낸다.
	 * 스킬의 데이터가 존재하지 않으면 익셉셥을 낸다.
	 * 진행중인 대화가 없으면 시작하면서 세션을 저장한다 : userid, bot, skill, step, savedata
	 */
	@PostMapping("/startskill")
	public Map<String, String> startSkill(@RequestParam String bot, @RequestParam String skill) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		String firstSectionJson = skillSessionService.startSkillAndGetFirstSection(bot, skill);
		result.put("skillchat", firstSectionJson);
		
		return result;
	}
	
	/*
	 * 유저가 스킬 진행중에 INPUT 을 만나서 그 중 하나를 선택하면 클라이언트가 이 API를 호출한다.
	 */
	@PostMapping("/choice")
	public Map<String, String> skillProcessChoice(@RequestParam String bot, @RequestParam String skill, @RequestParam String nextSection) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		String nextSectionJson = skillSessionService.skillProcessChoice(bot, skill, nextSection);
		result.put("skillchat", nextSectionJson);
		
		return result;
	}

	/*
	 * 유저가 스킬 진행중에 FEEDBACK 을 만나서 1~5중 하나를 선택하면 클라이언트가 이 API를 호출한다.
	 */
	@PostMapping("/feedback")
	public Map<String, String> skillProcessFeedback(@RequestParam String bot, @RequestParam String skill, @RequestParam String nextSection, @RequestParam int score) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		String nextSectionJson = skillSessionService.skillProcessFeedback(bot, skill, score, nextSection);
		result.put("skillchat", nextSectionJson);
		
		return result;
	}

	/*
	 * 유저가 스킬 진행중에 CHOICE를 만나서 그 중 하나를 선택하면 클라이언트가 이 API를 호출한다.
	 */
	@PostMapping("/input")
	public Map<String, String> skillProcessInput(@RequestParam String bot, @RequestParam String skill, @RequestParam String nextSection, @RequestParam String varName, @RequestParam String varValue) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		String nextSectionJson = skillSessionService.skillProcessInput(bot, skill, nextSection, varName, varValue);
		result.put("skillchat", nextSectionJson);
		
		return result;
	}
	
}
