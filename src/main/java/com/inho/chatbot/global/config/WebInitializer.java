package com.inho.chatbot.global.config;


import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

// web.xml을 대신하는 클래스.
// 스프링이 구동되면 가장 먼저 이 클래스가 호출되어, RootConfig -> ServletConfig 를 반영한다.

public class WebInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(RootConfig.class);
				
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		this.addDispatcherServlet(servletContext);
		this.addUtf8CharacterEncodingFilter(servletContext);
	}

	// dispatcher servlet을 추가한다.
	private void addDispatcherServlet(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.getEnvironment().addActiveProfile("dev");
		applicationContext.register(ServletConfig.class);
		
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",  new DispatcherServlet(applicationContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		dispatcher.setInitParameter("dispatchOptionsRequest", "true"); // OPTIONS 메소드로 요청을 보낼때 명령어가 출력되지 않게 함 : 약간의 보안에 도움. 참고 : https://m.blog.naver.com/PostView.nhn?blogId=duco777&logNo=220765466402&proxyReferer=https:%2F%2Fwww.google.com%2F
		
	}
	
	// UTF-8 인코딩 필터 추가.
	private void addUtf8CharacterEncodingFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic filter = servletContext.addFilter("CHARACTER_ENCODING_FILTER", CharacterEncodingFilter.class);
		filter.setInitParameter("encoding",  "UTF-8");
		filter.setInitParameter("forceEncoding",  "true");
		filter.addMappingForUrlPatterns(null,  false,  "/*");
	}
}
