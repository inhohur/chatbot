package com.inspot.workshadow.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@WebAppConfiguration
public class TestControllerTest {

	@Autowired
	private WebApplicationContext context;
	@Autowired
	public TestController testController;
	private MockMvc mockMvc;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.alwaysDo(MockMvcResultHandlers.print())
				.alwaysExpect(status().isOk()).build();
	}
	
	
	@Test
	public void testTestController() throws Exception {
        
        this.mockMvc.perform(get("/test/1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("objname"));
	}

	// DB 연결 테스트 
	// http://localhost:8080/workshadow/test/db
	@Test
	public void testDB() throws Exception {
		this.mockMvc.perform(get("/test/db"))
			.andExpect(status().isOk());
	}
	
	// mybatis 연결 테스트.
	@Test
	public void testMybatis() throws Exception {
		this.mockMvc.perform(get("/test/mybatis"))
			.andExpect(status().isOk());
	}
	
	

	// mybatis로 TEST 테이블 셀렉트테스트.
	@Test
	public void testMybatisSelect() throws Exception {
		this.mockMvc.perform(get("/test/selecttest"))
			.andExpect(status().isOk());
	}
	
}
