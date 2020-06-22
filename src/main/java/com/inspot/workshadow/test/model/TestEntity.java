package com.inspot.workshadow.test.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestEntity {
	private String name;
	private String id;
	private String pw;
}
