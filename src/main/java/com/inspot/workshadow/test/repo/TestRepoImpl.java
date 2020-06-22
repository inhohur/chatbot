package com.inspot.workshadow.test.repo;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.inspot.workshadow.test.model.TestEntity;

@Repository
public class TestRepoImpl implements TestRepo {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String namespace = "com.inspot.workshadow.mapper.TestMapper";
	
	@Override
	public List<TestEntity> selectTest() throws Exception {
		return sqlSession.selectList(namespace +".SelectTest");
	}
}
