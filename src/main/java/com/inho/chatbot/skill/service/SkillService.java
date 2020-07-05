package com.inho.chatbot.skill.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inho.chatbot.skill.dao.SkillDataDAO;
import com.inho.chatbot.skill.dao.SkillDataVO;
import com.inho.chatbot.user.service.UserSession;

@Service
public class SkillService {
	@Autowired
	private SkillDataDAO skillDataDAO;
	
	@Autowired
	private UserSession userSession;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<SkillDataVO> selectSkillDataByBotSkillSection(String bot, String skill, String section) throws Exception {
		Map<String,String> botSkillSection = new HashMap<String,String>();
		botSkillSection.put("bot", bot);
		botSkillSection.put("skill", skill);
		botSkillSection.put("section", section);
		
		return tagReplaceFilter(skillDataDAO.selectSkillDataByBotSkillSection(botSkillSection));
	}
	
	private List<SkillDataVO>  tagReplaceFilter(List<SkillDataVO> skillDataList) throws Exception {
		for(SkillDataVO skillDataVO : skillDataList) {
			if("TEXT".equals(skillDataVO.getType())) {
				String content = skillDataVO.getContent();
				content = content.replace("${username}", userSession.getUserNickname());
				skillDataVO.setContent(content);
			}
		}
		return skillDataList;
	}
	
}
