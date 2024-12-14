package com.mysite.sbb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//	=> 세 가지 어노테이션을 결합한 것
//		@SpringBootConfiguration: Spring의 설정 클래스로 동작하게 합니다.
//		@EnableAutoConfiguration: Spring Boot가 애플리케이션의 설정을 자동으로 구성하도록 합니다.
//		@ComponentScan: 지정된 패키지와 하위 패키지에서 Spring의 Bean을 자동으로 검색해 등록합니다.
public class SbbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbbApplication.class, args);
//		필요한 Bean들을 생성 및 주입합니다.
//		내장된 웹 서버(예: 톰캣)가 함께 시작되고, HTTP 요청을 처리할 준비가 완료됩니다.
	}

}
