package com.mysite.sbb.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mysite.sbb.BaseAuthorityValidator;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController extends BaseAuthorityValidator {

	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;

//	 현재 로그인한 사용자의 정보를 알려면 스프링 시큐리티가 제공하는 Principal 객체를 사용해야 한다.
//	 principal.getName()을 호출하면 현재 로그인한 사용자의 사용자명(사용자ID)을 알 수 있다.
//	
//	 <form th:action="@{|/answer/create/${question.id}|}" th:object="${answerForm}" method="post" class="my-3">

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
			BindingResult bindingResult, Principal principal) {

		Question question = this.questionService.getQuestion(id); //
		SiteUser siteUser = this.userService.getUser(principal.getName());

		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
//	    (Answer 객체에 대한 의존관계 : Question(One) <- Answer(Many), 댓글내용, 댓글 작성자)
		Answer newAnswer = this.answerService
				.create(question, answerForm.getContent(), siteUser);
		
//		<!-- 앵커 : 동일한 페이지 내에서 특정 위치로 스크롤 -->
//		<a th:id="|answer_${answer.id}|"></a>
		return String.format("redirect:/question/detail/%s#answer_%s", id, newAnswer.getId());
	}

//  <a th:href="@{|/answer/modify/${answer.id}|}" class="btn btn-sm btn-outline-secondary"

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modifyAnswer(AnswerForm answerForm, @PathVariable("id") Integer id,
			Principal principal) {

		Answer answer = this.answerService.getAnswer(id);
		SiteUser author = answer.getAuthor();

		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "수정권한 없음");

		answerForm.setContent(answer.getContent());

		return "answer_form";
	}

//  <form th:action="@{|/answer/modify/${answer.id}|}" th:object="${answerForm}" method ="post">

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modifyAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal) {

		if (bindingResult.hasErrors()) {
			return "answer_form";
		}

		Answer answer = this.answerService.getAnswer(id);
		SiteUser author = answer.getAuthor();

		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "수정권한 없음");

		this.answerService.modify(answer, answerForm.getContent());
		
//		<!-- 앵커 : 동일한 페이지 내에서 특정 위치로 스크롤 -->
//		<a th:id="|answer_${answer.id}|"></a>
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), id);
	}

//  th:data-uri="@{|/answer/delete/${answer.id}|}"

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {

		Answer answer = this.answerService.getAnswer(id);
		SiteUser author = answer.getAuthor();

		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "삭제권한 없음");

		this.answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}

//  th:data-uri="@{|/answer/vote/${answer.id}|}"

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id) {

		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());

		this.answerService.vote(answer, siteUser);
		
//		<!-- 앵커 : 동일한 페이지 내에서 특정 위치로 스크롤 -->
//		<a th:id="|answer_${answer.id}|"></a>
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), id);
	}

//	@PostMapping("/create/{id}")
//	public String createAnswer(Model model, @PathVariable(value = "id") Integer id,
//			@RequestParam(value = "content") String content) {
//
//		Question question = this.questionService.getQuestion(id);
//		answerService.create(question, content);
//
//		return String.format("redirect:/question/detail/%s", id);
//	}

//	@PostMapping("/create/{id}")
//	public String createAnswer(Model model, @PathVariable("id") Integer id, 
//			@Valid AnswerForm answerForm, BindingResult bindingResult) {
//
//		Question question = this.questionService.getQuestion(id);
//
//		if (bindingResult.hasErrors()) {
//			model.addAttribute("question", question);
//			return "question_detail";
//		}
//	
//		this.answerService.create(question, answerForm.getContent());
//		return String.format("redirect:/question/detail/%s", id);
//	}

}
