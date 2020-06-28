package com.inho.chatbot.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

// root-context.xml 의 역할을 하는 클래스.
// DataSource 설정등, 웹이 아니더라도 필요한 설정은 여기서 설정한다.

@Configuration
@Import({
	DataSourceConfig.class,
	SqlMapperConfig.class
})
@PropertySource("classpath:config/global.properties")
public class RootConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		return ppc;
	}
	
	
	
}
