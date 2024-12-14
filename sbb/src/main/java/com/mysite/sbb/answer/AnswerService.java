package com.mysite.sbb.answer;

import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;

	@Transactional
	public Answer create(Question question, String content, SiteUser author) {
		Answer answer = Answer.createInstance(question, content, author);

		// 답변을 저장하고, 자동으로 Question의 answerList에 추가됨
		return this.answerRepository.save(answer);
	}

	public Answer getAnswer(Integer id) {
		String message = String.format("Answer with ***id %d*** Not Found", id);
		
		return this.answerRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException(message));
	}
	
	public void modify(Answer answer, String content) {
		answer.modify(content);
		this.answerRepository.save(answer);
	}

	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}
	
	public void vote(Answer answer, SiteUser siteUser) {
		answer.vote(siteUser);
		this.answerRepository.save(answer);
	}
	
}
