package com.inspot.workshadow.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inspot.workshadow.test.model.TestEntity;
import com.inspot.workshadow.test.model.TestParam;
import com.inspot.workshadow.test.service.TestService;

@Controller
@RequestMapping("/test")
@ContextConfiguration(locations = { "file:src/main/java/com/inspot/workshadow/config/datasource.xml" })
//@ContextConfiguration(locations={"classpath:com/inspot/workshadow/config/datasource.xml"})
public class TestController {
	@Autowired
	private TestService testService;
	@Autowired
	private DataSource ds;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@GetMapping("/add")
	public String addObj() {
		testService.add("addok");
		return "test/addok";
	}
	
	@PutMapping
	public String updateObj(@RequestBody TestParam param) {
		TestEntity test = TestEntity.builder().name("putuser").build();
		
		testService.add(test);
		return "test/putok";
	}
	
	// 테스트할 때 : http://localhost:8080/workshadow/test/3?name=111
	@GetMapping("{id}")
	public String getOneObj(@PathVariable Long id, TestParam testParam, Model model) {
 		TestEntity test = testService.getOneUser();
		if(test == null) {
			testService.add("therewasnouser");
			test = testService.getOneUser();
		}
		model.addAttribute("name", testParam.getName());
		model.addAttribute("objname", test.getName());
		model.addAttribute("id", id);
		return "test/get";
	}

	@GetMapping
	public String getOneObj(Model model) {
		return "test/addok";
	}

	// DB 테스트용 코드.
	// http://localhost:8080/workshadow/test/db
	@GetMapping("/db")
	public String testDB(Model model) throws Exception {
		testConnection();
		ds.getConnection();
		return "test/db";
	}
	
	// Mybatis 테스트 코드.
	// http://localhost:8080/workshadow/test/mybatis
	@GetMapping("/mybatis")
	public String testMyBatis(Model model) {
		try {
			SqlSession sqlSession = sqlSessionFactory.openSession();
			System.out.println(sqlSession);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "test/db";
	}
	
	// mybatis를 이용해서 test 테이블에서 데이터를 쿼리해본다.
	// http://localhost:8080/workshadow/test/selecttest
	@GetMapping("/selecttest")
	public String testSelect(Model model) {
		try {
			List<TestEntity> tests = testService.getAllTest();
			model.addAttribute("log", tests.toString());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "test/db";
	}
	

	private void testConnection() throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/newshadow?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul", "root", "wings0000");
		System.out.println("TESTCONNECTION OK");
		System.out.println(conn);
	}
	
}
