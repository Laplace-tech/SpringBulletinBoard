package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
//@Controller가 HTTP 요청을 처리하는동안, @Service는 비즈니스 로직을 수행하고 
//데이터베이스와의 상호작용을 관리함. @Controller는 @Service의 결과를 받아서 적절한 HTTP response를 생성함.
@RequiredArgsConstructor
//클래스의 모든 final 필드와 @NonNull로 지정된 필드에 대한 생성자를 자동으로 생성함.
//@Service와 @RequiredArgsConstructor를 함께 사용하면 비즈니스 로직을 처리하는 서비스 클래스에서
//의존성을 간편하게 주입받을 수 있으며, 코드가 간결해지고 명확하게 유지됨.
public class QuestionService {

	private final QuestionRepository questionRepository;
	
	
	
//	호출부 : QuestionController
//
//	@PostMapping("/create") // create URL로 오는 POST 요청을 처리
//		ㄴ> questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser)

	// 새로운 질문 생성
	public Question create(String subject, String content, SiteUser author) {
		Question question = Question.createInstance(subject, content, author);
		return questionRepository.save(question);
	}

	
	
//	호출부 : QuestionController
//
//  @GetMapping("/detail/{id})
//		ㄴ> Question question = questionService.getQuestion(id);

    // 질문 ID로 특정 질문을 찾음
    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(createNotFoundMessage(id)));
    }

    private String createNotFoundMessage(Integer id) {
        return String.format("Question with id %d not found", id);
    }

    
    
    
//  호출부 : QuestionController
//
//  @GetMapping("/list") 
//		ㄴ> Page<Question> paging = questionService.getList(pageNumber)

	// 페이지네이션된 질문 목록 반환
	public Page<Question> getList(int pageNumber, String kwd) {
		return this.questionRepository.findAllByKeyword(kwd, createPageRequest(pageNumber));
	}
	
	private Pageable createPageRequest(int pageNumber) {
		return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createDate")));
	}
	
	
	
//	호출부 : QuestionController
//	
//@PostMapping("/modify/{id}")
//	ㄴ> this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

	public void modify(Question question, String subject, String content) {
		question.modify(subject, content);
		this.questionRepository.save(question);
	}

	
	
//	호출부 : QuestionController
//	 
//@GetMapping("/delete/{id}")
//	ㄴ> this.questionService.delete(question);

	public void delete(Question question) {
		this.questionRepository.delete(question);
	}

	
	
	
//	호출부 : QuestionController
//
//   @GetMapping("/vote/{id}")
//		ㄴ> this.questionService.vote(question, siteUser)

	public void vote(Question question, SiteUser siteUser) {
		question.vote(siteUser);
		this.questionRepository.save(question);
	}

}
