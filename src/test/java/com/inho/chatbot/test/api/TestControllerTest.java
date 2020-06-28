package com.inho.chatbot.test.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.inho.chatbot.global.config.RootConfig;
import com.inho.chatbot.global.config.ServletConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, ServletConfig.class})
@WebAppConfiguration
public class TestControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(MockMvcResultHandlers.print())
				.alwaysExpect(status().isOk()).build();
	}
	
	/*
	 * http://localhost:8080/workshadow/test/methodtest
	 */
	@Test
	public void methodGetTest() throws Exception {
		this.mockMvc.perform(get("/test/methodtest"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("method").value("get"));
	}
	
	@Test
	public void methodPutTest() throws Exception {
		this.mockMvc.perform(put("/test/methodtest"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("method").value("put"));
	}
	
	@Test
	public void methodPostTest() throws Exception {
		this.mockMvc.perform(post("/test/methodtest"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("method").value("post"));
	}
	
	@Test
	public void methodDeleteTest() throws Exception {
		this.mockMvc.perform(delete("/test/methodtest"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("method").value("delete"));
	}

	@Test
	public void loggerTest() throws Exception {
		logger.trace("TestControllerTest trace : " + logger.toString());
		logger.debug("TestControllerTest debug : " + logger.toString());
		logger.info("TestControllerTest info : " + logger.toString());
		logger.warn("TestControllerTest warn : " + logger.toString());
		logger.error("TestControllerTest error : " + logger.toString());
	}

	// 가장 기본적 JDBC 커넥션을 테스트해보기 위해 만든 함수.
	@Test
	public void basicJDBCTest() throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/CHATBOT?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul", "root", "wings0000");
		conn.createStatement();
	}

	/*
	 * http://localhost:8080/workshadow/test/dbconnectiontest
	 */
	@Test
	public void dbConnectionTest() throws Exception {
		this.mockMvc.perform(get("/test/dbconnectiontest"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("result").value("ok"));
	}


	/*
	 * MyBatis를 이용하여, TEST 테이블에서 데이터 가져오는 것을 테스트해본다.
	 * http://localhost:8080/workshadow/test/selecttestlist
	 */
	@Test
	public void selectTestListTest() throws Exception {
		this.mockMvc.perform(get("/test/selecttestlist"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("count").value("3"));
	}

}
