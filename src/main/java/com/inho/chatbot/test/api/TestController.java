package com.inho.chatbot.test.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inho.chatbot.test.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private TestService testService;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Environment environment;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@GetMapping("/methodtest")
//	public String methodTestGet(Model model) {
//		model.addAttribute("result", "get");
//		return "/test/methodtest";
//	}
	
	@GetMapping("/methodtest")
	public Map<String, String> methodTestGet() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("method", "get");
		return result;
	}
	
	@PutMapping("/methodtest")
	public Map<String, String> methodTestPut(Model model) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("method", "put");
		return result;
	}

	@PostMapping("/methodtest")
	public Map<String, String> methodTestPost(Model model) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("method", "post");
		return result;
	}

	@DeleteMapping("/methodtest")
	public Map<String, String> methodTestDelete(Model model) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("method", "delete");
		return result;
	}

	@GetMapping("/dbconnectiontest")
	public Map<String, String> dbConnectionTest(Model model) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		result.put("result", "ok");
		return result;
	}
	
	@GetMapping("/selecttestlist")
	public Map<String, String> selectTestList(Model model) throws Exception {
		Map<String, String> result = new HashMap<String, String>();

		List<?> tests = testService.selectTestList();
		result.put("count",  String.valueOf(tests.size()));
		return result;
	}
}
