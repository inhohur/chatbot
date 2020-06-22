package com.inspot.workshadow.test.repo;

import java.util.List;

import com.inspot.workshadow.test.model.TestEntity;

public interface TestRepo {
	public List<TestEntity> selectTest() throws Exception;
}
