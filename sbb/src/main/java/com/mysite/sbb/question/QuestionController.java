package com.mysite.sbb.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.BaseAuthorityValidator;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
// Controller : HTTP 요청을 처리하고 사용자의 요청을 @Service에 전달하는 역할을 함.
// 즉, 사용자의 입력을 받아서 QuestionService의 메서드를 호출하여 비즈니스 로직을 실행함.
@RequestMapping("/question")
@RequiredArgsConstructor
// @RequiredArgsConstructor를 사용하면 생성자를 자동으로 생성
public class QuestionController extends BaseAuthorityValidator {

//  final로 선언되어 있으므로, 생성자를 통해서만 초기화됩니다.
	private final QuestionService questionService;
	private final UserService userService;

	
//	@GetMapping("/")
//	public String root() {
//		return "redirect:/question/list";
//	}

	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int pageNumber, 
			@RequestParam(value = "kw", defaultValue = "") String kwd) {

		Page<Question> paging = questionService.getList(pageNumber, kwd);

//						*** at QuestionService method ***
//
//		public Page<Question> getList(int pageNumber) {
//
//			List<Sort.Order> sorts = new ArrayList<>();
//			sorts.add(Sort.Order.desc("createDate")); // 게시글 작성일을 기준으로 내림차순 = 최신순
//			Pageable pageable = PageRequest.of(pageNumber, 12, Sort.by(sorts));
//
//			return this.questionRepository.findAll(pageable);
//		}

		model.addAttribute("paging", paging);
		model.addAttribute("kw", kwd);
//             			   *** question_list.html file ***  
//
//		<tr th:each="question, loop : ${paging}">
//
//      <a th:href="@{|/question/detail/${question.id}|}" >>>> (Ex : /question/detail/1)
//         th:text="${question.subject}"></a>

		return "question_list";
	}

	
//  <a th:href="@{|/question/detail/${question.id}|}" th:text="${question.subject}"></a>

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,
			AnswerForm answerForm /* Spring Framework의 폼 바인딩 기능 */) {
		Question question = questionService.getQuestion(id);

//						*** QuestionService's method ***
//
//		질문 ID로 특정 질문을 찾음
//		public Question getQuestion(Integer id) {
//			return this.questionRepository.findById(id) //이떄 리턴타입은 Optional<Question>
//					.orElseThrow(() -> new DataNotFoundException("질문을 못찾겠어요..!"));
//		}

		model.addAttribute("question", question); // Spring MVC에서 뷰에 데이터를 전달하기 위한 코드
//      QuestionController에서 찾은 Question 데이터를 뷰로 전달해서
// 		질문의 상세 정보를 동적으로 웹페이지에 보여주는 데 사용.

		return "question_detail";
	}

//	<!-- 질문 등록 버튼 -->
//	<a th:href="@{/question/create}" class="btn btn-primary">질문 등록하기</a>

	@PreAuthorize("isAuthenticated()") // 로그인된 사용자만 이 메소드를 호출할 수 있음
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form"; 
	}

//  <form th:action="@{/question/create}" th:object="${questionForm}" method="post">

	@PreAuthorize("isAuthenticated()") // 로그인된 사용자만 이 메소드를 호출할 수 있음
	@PostMapping("/create") // create URL로 오는 POST 요청을 처리
	public String questionCreate(@Valid QuestionForm questionForm, // @Valid : 사용자로부터 입력받은 질문 폼 데이터를 검증
			BindingResult bindingResult, // 폼에 에러가 발생하면 이 객체에 담기고, 메소드에서 에러를 확인할 수 있습니다.
			Principal principal) {

		if (bindingResult.hasErrors()) {
			return "question_form";
		}

		SiteUser siteUser = userService.getUser(principal.getName());
		questionService.create(questionForm.getSubject(), // 질문 제목 등록
						       questionForm.getContent(), // 질문 내용 등록
						       siteUser); // 게시자 등록

		return "redirect:/question/list";
	}

	
	
//  *** question_detail.html *** 
//  		ㄴ> <a th:href="@{|/question/modify/${question.id}|}" 

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id,
			Principal principal) {

		Question question = this.questionService.getQuestion(id);
		SiteUser author = question.getAuthor();
		
		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "수정권한 없음");
		
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		
		return "question_form";
	}

//  *** question_detail.html ***
// 			ㄴ> <form th:object="${questionForm}" method ="post">

	@PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
	@PostMapping("/modify/{id}") // /question/modify/{id} 에서 날라온 Post 요청을 처리
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {

		if (bindingResult.hasErrors()) {
			return "question_form"; // bindingResult는 유효성 검사 결과의 내용을 담고있음
		}

		Question question = this.questionService.getQuestion(id);
		SiteUser author = question.getAuthor();
		
		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "수정권한 없음");
		
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}

	
	
//  *** question_detail.html ***
//			ㄴ> <a href="javascript:void(0);" th:data-uri="@{|/question/delete/${question.id}|"
	 
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		
		Question question = this.questionService.getQuestion(id);
		SiteUser author = question.getAuthor();
	
		validateUserAuthority(author, principal, HttpStatus.FORBIDDEN, "삭제권한 없음");
		
		this.questionService.delete(question);
		return "redirect:/";
	}
	
//  *** question_detail.html ***
//  		ㄴ> th:data-uri="@{|question/vote/${question.id}|}">

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	
	
//	*** @Deprecated ***
//
//	@PostMapping("/create")
//	public String questionCreate(@RequestParam(value = "subject") String subject,
//								 @RequestParam(value = "content") String content) {
//		
//		this.questionService.create(subject, content);
//		return "redirect:/question/list";
//	}
//
//	@GetMapping("/list")
//	public String list(Model model) {
//		List<Question> questionList = this.questionService.getList();
//		model.addAttribute("questionList", questionList);
//
//		return "question_list";
//	}

}
