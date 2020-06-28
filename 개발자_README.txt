개발에 들어가기 전 세팅 ------------------------------------

1. git 에서 프로젝트를 clone
2. src/main/resources/log4j2.xml 에서 log file의 위치를 자신의 PC에 맞게 수정.
3. 로컬에 MARIADB 를 설치한 후, CHATBOT 이라는 database를 만들고, src/main/resources/db/*.DDL.sql, *.DML.sql 을 실행.
4. TestControllerTest.java 를 유닛테스트 돌려보고, 잘 동작하면 tomcat 으로 구동.


패키지 구조 -----------------------------------

전자정부에서 많이 사용하는 구조를 채택했습니다. 

com.inspot.newprj.user.web
	UserController.java
com.inspot.newprj.user.service
	UserService.java
	UserVO.java
com.inspot.newprj.user.service.impl
	UserServiceImpl.java
	UserMapper.java			
		// 전자정부에서는 처음에 iBatis를 사용하다 MyBatis 양쪽을 지원하고 있는데,  
		// iBatis 용으로 작성한 것에는 DAO, MyBatis 용으로 작성한 것에는 Mapper를 붙였습니다.
		// 우리는 MyBatis를 사용할 것이므로, 혼란이 없도록 Mapper를 붙입니다.
	

스프링 설정 XML ------------------------------

XML 파일에 설정이 늘어남에 따라, 스프링이 버전업되면서 자바 클래스로 편입시키는 추세입니다.
web.xml, root-context.xml, servlet-context.xml을 없애고, com.inspot.workshadow.config 아래에 클래스로 대체했습니다.
그 안에 있는 클래스들을 살펴보면, 수정할 때 어느 파일을 건드려야 하는지 쉽게 보입니다.
DB 접속정보는 src/main/resources/config/global.properties 에서 편집합니다.

로깅-------------------------------------

slf4j 라는 wrapper  라이브러리를 이용. 실제 작동하는 로깅은 log4j2를 사용. 둘 다 전자정부에 포함되어 있습니다.
slf4j 를 사용하는 이유 혹시 나중에 log4j2를 다른 로그 라이브러리로 변경하더라도 소스코드를 수백개씩 변경하지 않아도 되기 때문입니다.

사용법은 아래와 같습니다.

// Logger 가 log4j가 아니라, slf4j 에서 import 되게 하는데 주의.  
private Logger logger = LoggerFactory.getLogger(this.getClass());

public void dosomething() { 
	logger.debug("디버깅 메시지");
}

* System.out.println 사용 금지.
나중에 이걸 한꺼번에 빼야 하는데, 변경이력이 지저분해집니다.
그렇다고 안 빼면, 운영할 때 서버 부하 걸립니다. 




