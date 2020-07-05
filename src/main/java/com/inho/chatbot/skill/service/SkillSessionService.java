package com.inho.chatbot.skill.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inho.chatbot.global.exception.AppException;
import com.inho.chatbot.global.exception.ExceptionCode;
import com.inho.chatbot.skill.dao.SkillDataVO;
import com.inho.chatbot.skill.dao.SkillSessionDAO;
import com.inho.chatbot.skill.dao.SkillSessionVO;
import com.inho.chatbot.user.service.UserSession;

@Service
public class SkillSessionService {
	@Autowired
	SkillService skillService;
	
	@Autowired
	SkillSessionDAO skillSessionDAO;
	
	@Autowired
	UserSession userSession;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public String startSkillAndGetFirstSection(String bot, String skill) throws Exception {
		
		// 먼저 스킬 세션에 해당 봇과 진행중인 스킬 세션이 있나 조회한다. 있으면 안 된다 -> 익셉션.
		checkThereIsNoSkillInProgress(bot);
		
		
		String sectionStart = "START";
		
		// 해당 봇에 해당 스킬이 있는지 조회한다. 없으면 익셉션.
		List<SkillDataVO> skillDataList = skillService.selectSkillDataByBotSkillSection(bot, skill, sectionStart);
		if(skillDataList.size() == 0) 
			throw new AppException(ExceptionCode.InvalidSkillOrBotWhenStartingSkill);
		
		// 세션을 기록한다.
		insertSkillSessionByUserBotSkillSection(userSession.getUserId(), bot, skill, sectionStart);

		Gson gson = new Gson();
		return gson.toJson(skillDataList);
	}


	private void insertSkillSessionByUserBotSkillSection(String userId, String bot, String skill, String currentSection) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("userid", userId);
		param.put("bot", bot);
		param.put("skill", skill);
		param.put("currentsection", currentSection);

		skillSessionDAO.insertSkillSessionByUserBotSkillSection(param);
	}
	
	private void updateSectionOfSkillSessionByUserBotSkill(SkillSessionVO skillSessionVO) throws Exception {
		skillSessionDAO.updateSectionOfSkillSessionByUserBotSkill(skillSessionVO);
	}
	
	private void deleteSkillSessionByUserBot(String userId, String bot) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("userid", userId);
		param.put("bot", bot);

		skillSessionDAO.deleteSkillSessionByUserBot(param);
	}
	
	public SkillSessionVO selectSkillSessionByUserBot(String userId, String bot) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("userid", userId);
		param.put("bot", bot);

		return skillSessionDAO.selectSkillSessionByUserBot(param);
	}
	
	

	public String skillProcessChoice(String bot, String skill, String nextSection) throws Exception {

		SkillSessionVO skillSessionVO = selectSkillSessionByUserBot(userSession.getUserId(), bot);
		
		// 진행중인 스킬이 세션에 기록되어 있지 않으면 잘못된 것.
		if(skillSessionVO == null) 
			throw new AppException(ExceptionCode.NoSkillSessionInProgress);
		
		// 세션이 기록된 스킬이, 지금 들어온 인풋하고 다르면 잘못된 것.
		if(skill == null || !skill.equals(skillSessionVO.getSkill())) 
			throw new AppException(ExceptionCode.DifferentSkillInProgress);
		
		// 해당 봇에 해당 스킬이 있는지 조회한다. 없으면 익셉션.
		List<SkillDataVO> skillDataList = skillService.selectSkillDataByBotSkillSection(bot, skill, nextSection);
		if(skillDataList.size() == 0) 
			throw new AppException(ExceptionCode.NoSkillDataForTheChocie);
		
		// 세션을 업데이트한다. 
		skillSessionVO.setCurrentSection(nextSection);
		updateSectionOfSkillSessionByUserBotSkill(skillSessionVO);
		
		// 데이터중에 종료가 있으면(EXIT) 스킬 세션을 종료시킨다.
		checkExitAndProcess(bot, skillDataList);
		

		Gson gson = new Gson();
		return gson.toJson(skillDataList);
	}


	private void checkExitAndProcess(String bot, List<SkillDataVO> skillDataList) throws Exception {
		for(SkillDataVO skillDataVO : skillDataList) {
			if("EXIT".equals(skillDataVO.getType())) {
				deleteSkillSessionByUserBot(userSession.getUserId(), bot);
			}
		}
		
	}


	public String skillProcessInput(String bot, String skill, String nextSection, String varName, String varValue) throws Exception {
		
		SkillSessionVO skillSessionVO = selectSkillSessionByUserBot(userSession.getUserId(), bot);
		
		// 진행중인 스킬이 세션에 기록되어 있지 않으면 잘못된 것.
		if(skillSessionVO == null) 
			throw new AppException(ExceptionCode.NoSkillSessionInProgress);
		
		// 세션이 기록된 스킬이, 지금 들어온 인풋하고 다르면 잘못된 것.
		if(skill == null || !skill.equals(skillSessionVO.getSkill())) 
			throw new AppException(ExceptionCode.DifferentSkillInProgress);
		
		// 해당 봇에 해당 스킬이 있는지 조회한다. 없으면 익셉션.
		List<SkillDataVO> skillDataList = skillService.selectSkillDataByBotSkillSection(bot, skill, nextSection);
		if(skillDataList.size() == 0) 
			throw new AppException(ExceptionCode.NoSkillDataForTheChocie);

		// 입력 변수가 들어온 경우 그걸 세션에 저장한다.
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();
		if(skillSessionVO.getSessionVariables() != null) {
			jsonObj = gson.fromJson(skillSessionVO.getSessionVariables(), JsonObject.class);
		}
		jsonObj.addProperty(varName, varValue);
		skillSessionVO.setSessionVariables(gson.toJson(jsonObj));
		
		// 세션을 업데이트한다. 
		skillSessionVO.setCurrentSection(nextSection);
		updateSectionOfSkillSessionByUserBotSkill(skillSessionVO);
		
		// 스킬데이터 내의 변수들을 값으로 치환한다.
		replaceSessionVariablesIntoValues(skillDataList, jsonObj);
		
		// 데이터중에 종료가 있으면(EXIT) 스킬 세션을 종료시킨다.
		checkExitAndProcess(bot, skillDataList);
		
		return gson.toJson(skillDataList);
	}
	
	private void replaceSessionVariablesIntoValues(List<SkillDataVO> skillDataList, JsonObject sessionVariables) throws Exception {
		for(SkillDataVO skillDataVO : skillDataList) {
			if("TEXT".equals(skillDataVO.getType())) {
				String content = skillDataVO.getContent();
				for(Map.Entry<String, JsonElement> entry : sessionVariables.entrySet()) {
					content = content.replace("${" + entry.getKey() + "}", entry.getValue().toString());
				}
				skillDataVO.setContent(content);
			}
		}
	}


	public void checkThereIsNoSkillInProgress(String bot) throws Exception {
		SkillSessionVO skillSessionVO = selectSkillSessionByUserBot(userSession.getUserId(), bot);
		if(skillSessionVO != null) 
			throw new AppException(ExceptionCode.AlreadySkillInProgress);
	}


	public String skillProcessFeedback(String bot, String skill, int score, String nextSection) throws Exception {
		
		SkillSessionVO skillSessionVO = selectSkillSessionByUserBot(userSession.getUserId(), bot);
		
		// 진행중인 스킬이 세션에 기록되어 있지 않으면 잘못된 것.
		if(skillSessionVO == null) 
			throw new AppException(ExceptionCode.NoSkillSessionInProgress);
		
		// 세션이 기록된 스킬이, 지금 들어온 인풋하고 다르면 잘못된 것.
		if(skill == null || !skill.equals(skillSessionVO.getSkill())) 
			throw new AppException(ExceptionCode.DifferentSkillInProgress);
		
		// 해당 봇에 해당 스킬이 있는지 조회한다. 없으면 익셉션.
		List<SkillDataVO> skillDataList = skillService.selectSkillDataByBotSkillSection(bot, skill, nextSection);
		if(skillDataList.size() == 0) 
			throw new AppException(ExceptionCode.NoSkillDataForTheChocie);

		// score 를 서버에 저장한다. 현재는 로그만 남긴다.
		logger.trace("SAVE SCORE : " + score);
		
		// 세션을 업데이트한다. 
		skillSessionVO.setCurrentSection(nextSection);
		updateSectionOfSkillSessionByUserBotSkill(skillSessionVO);
		
		// 데이터중에 종료가 있으면(EXIT) 스킬 세션을 종료시킨다.
		checkExitAndProcess(bot, skillDataList);
		
		Gson gson = new Gson();
		return gson.toJson(skillDataList);	
	}
}
