package com.inho.chatbot.chat.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inho.chatbot.global.config.RootConfig;
import com.inho.chatbot.global.config.ServletConfig;
import com.inho.chatbot.global.exception.ExceptionCode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, ServletConfig.class})
@WebAppConfiguration
public class ChatControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(MockMvcResultHandlers.print())
				.alwaysExpect(status().isOk()).build();
	}
	
	/*
	 * 봇에게 말을 걸어보는 테스트.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void userChatTest() throws Exception {
		this.mockMvc.perform(post("/chat/user").param("bot", "LAMAMA").param("text", "안녕"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.botchat").exists());
	}

	/*
	 * 봇에게 말을 걸때 이름이 잘못된 경우 익셉션 처리 테스트.
	 */
	@Test
	public void userChatTestInvalidBot() throws Exception {
		this.mockMvc.perform(post("/chat/user").param("bot", "ZZZZ").param("text", "안녕"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").exists())
			.andExpect(jsonPath("$.code").value(ExceptionCode.InvalidBotName.code));
	}
	

	/*
	 * 스킬 시작 테스트.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void startSkillTest() throws Exception {
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
	}
	
	/*
	 * 스킬 진행 중에 다시 시작 API 를 호출받으면 -> 익셉션.
	 */
	@Test
	@Sql("classpath:db/test/startskillwhenalreadyinprogresstest_before.sql")
	public void startSkillWhenAlreadyInProgressTest() throws Exception {
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(ExceptionCode.AlreadySkillInProgress.code));
	}
	
	/*
	 * 스킬 시작 테스트. 잘못된 스킬 이름으로 호출 익셉션 테스트 -> 잘못되었다는 것은 스킬데이터에 해당 봇 & 스킬 조합으로 입력된 데이터가 없다는 걸 의미한다.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void startSkillTestInvalidSkill() throws Exception {
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOMEONE"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.code").exists())
		.andExpect(jsonPath("$.code").value(ExceptionCode.InvalidSkillOrBotWhenStartingSkill.code));
	}
	

	/*
	 * 스킬 시작하고 첫 분기에서 아니 선택 -> 스킬 종료이 종료된다.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void startSkillChooseNoExitTest() throws Exception {
		// 먼저 스킬 시작.
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
		
		// 유저가 나중에를 선택했다. 클라이언트에서는 선택된 테스트에 맞는 GOTO 태그를 보내주면 된다.
		this.mockMvc.perform(post("/chat/choice").param("bot", "LAMAMA").param("skill", "SOME").param("nextSection", "PART2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
	}

	/*
	 * 스킬 시작하고 첫 분기에서 유저가 채팅을 시도한다. -> 이미 스킬 진행중이어서 안된다는 익셉션.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void startUserChatDuringSkillTest() throws Exception {
		// 먼저 스킬 시작.
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
		
		// 스킬 진행중에 채팅을 시도하면 익셉션.
		this.mockMvc.perform(post("/chat/user").param("bot", "LAMAMA").param("text", "안녕"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.code").value(ExceptionCode.AlreadySkillInProgress.code));
		
	}
	
	/*
	 * 스킬의 첫번째부터 플로우를 쭉 따라가서 엔딩을 보고, 일반 채팅 메시지까지 날리도록 한다.
	 */
	@Test
	@Sql("classpath:db/test/startskilltest_before.sql")
	public void startSkillChoosYesTest() throws Exception {
		// 먼저 스킬 시작.
		this.mockMvc.perform(post("/chat/startskill").param("bot", "LAMAMA").param("skill", "SOME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
		
		// 유저가 나중에를 선택했다. 클라이언트에서는 선택된 테스트에 맞는 GOTO 태그를 보내주면 된다.
		this.mockMvc.perform(post("/chat/choice").param("bot", "LAMAMA").param("skill", "SOME").param("nextSection", "PART1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.skillchat").exists());
		
		// 유저가 someone 이라는 변수를 입력했다. 
		this.mockMvc.perform(post("/chat/input").param("bot", "LAMAMA").param("skill", "SOME").param("nextSection", "PART3").param("varName", "somename").param("varValue", "김썸녀"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.skillchat").exists());

		// 유저가 33 번 타로카드를 뽑았다.
		this.mockMvc.perform(post("/chat/choice").param("bot", "LAMAMA").param("skill", "SOME").param("nextSection", "TAROT33"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.skillchat").exists());

		// 유저가 피드백 5점을 주었다.
		this.mockMvc.perform(post("/chat/feedback").param("bot", "LAMAMA").param("skill", "SOME").param("score","5").param("nextSection", "PART4"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.skillchat").exists());
		
		// 유저가 아니나중에를 선택했다.
		this.mockMvc.perform(post("/chat/choice").param("bot", "LAMAMA").param("skill", "SOME").param("nextSection", "PART12"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.skillchat").exists());

		// 스킬이 끝난 상태라 일반 채팅 모드로 들어간다.	
		this.mockMvc.perform(post("/chat/user").param("bot", "LAMAMA").param("text", "안녕"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.botchat").exists());
		
	}
	
}
