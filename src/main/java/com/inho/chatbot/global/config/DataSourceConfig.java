package com.inho.chatbot.global.config;


import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	
	@Value("${jdbc.url}")
	private String jdbcUrl;
	
	@Value("${jdbc.username}")
	private String jdbcUsername;
	
	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Bean
	public DataSource dataSource() throws Exception {
		logger.debug("jdbc driver name : " + jdbcDriver);
		Class.forName(jdbcDriver);

		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(jdbcUrl);
		ds.setUsername(jdbcUsername);
		ds.setPassword(jdbcPassword);
		
		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
	}
}
