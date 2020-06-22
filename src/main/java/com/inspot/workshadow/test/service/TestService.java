package com.inspot.workshadow.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inspot.workshadow.test.model.TestEntity;
import com.inspot.workshadow.test.repo.TestRepo;

@Service
public class TestService {
	@Autowired
	private TestRepo testRepo;
	
	private static TestEntity uservo;
	
	public void add(String newusername) {
		uservo = TestEntity.builder().name(newusername).build();
	}
	
	public void add(TestEntity test) {
		uservo = test;
	}

	public TestEntity getOneUser() {
		return uservo;
	}
	
	public List<TestEntity> getAllTest() throws Exception {
		return testRepo.selectTest();
	}
	
//	private <R, T> T sourceToDestinationTypeCasting(R source, T destination) {
//		modelMapper.map(source, destination);
//		return destination;
//	}
}
