package com.mysite.sbb.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

	private final UserService userService;

//  navbar.html : <a class="nav-link" th:href="@{/user/signup}">회원가입</a> 
	@GetMapping("/signup")
	public String signUp(UserCreateForm userCreateForm) {
		return "signup_form";
	}

//  signup_form : <form th:action="@{/user/signup}" th:object="${userCreateForm}" method="post">
	@PostMapping("/signup")
	public String signUp(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "signup_form";
		}

		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않음");
			return "signup_form";
		}

		try {
			this.userService.create(userCreateForm.getUsername(), // 유저 이름
					userCreateForm.getEmail(), // 유저 이메일
					userCreateForm.getPassword1() // 유저 비밀번호
			);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("signupFailed", "이미 등록된 사용자인데...");
			return "signup_form";
		} catch (Exception e) {
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}

		return "redirect:/";
	}
	
	
//  navbar.html : <a class="nav-link" sec:authorize="isAnonymous()" th:href="@{/user/login}">로그인</a>
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}

}
