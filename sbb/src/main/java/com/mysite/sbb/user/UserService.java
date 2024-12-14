package com.mysite.sbb.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

// Lombok의 생성자 자동 생성 : final 필드에 대해 생성자를 자동으로 생성함.
@RequiredArgsConstructor
@Service
public class UserService {

	// 사용자 데이터 접근을 위한 UserRepository
	private final UserRepository userRepository;

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	Bean 등록 
//	: @Bean 애너테이션으로 정의된 메서드는 Spring 컨테이너에 의해 관리되는 Bean을 생성함.
//	  즉, SecurityConfig 클래스에서 정의된 PasswordEncoder는 Spring 컨테이너에 등록되고, 
//	  이 빈을 다른 클래스에서 사용할 수 있게 함.
//
//	PasswordEncoder가 SecurityConfig에서 @Bean으로 등록되었기 때문에, 
//	Spring은 UserService의 생성자가 호출될 때, @Bean PasswordEncoder을 주입함

	private final PasswordEncoder passwordEncoder; // 의존성 주입(Dependency Injection)

// 	*** 사용자 생성 메서드 ***
//  
//	(호출부)
//	this.userService.create(userCreateForm.getUsername(), // 유저 이름
//			userCreateForm.getEmail(), // 유저 이메일
//			userCreateForm.getPassword1() // 유저 비밀번호

	public SiteUser create(String username, String password, String email) {
		SiteUser user = SiteUser.createInstance(username, passwordEncoder.encode(password), email);
		return this.userRepository.save(user);
	}

// 	*** 사용자 조회 메서드 ***
	
	public SiteUser getUser(String userName) {
		return this.userRepository.findByUserName(userName)
				.orElseThrow(() -> new DataNotFoundException(String.format("Question with user_name %s not found", userName)));
	}

}
