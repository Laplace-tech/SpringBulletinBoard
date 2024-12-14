package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Question {

	// ID (Question 객체의 기본키)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// SUBJECT (질문 제목)
	@Column(length = 200, nullable = false)
	private String subject;

	// CONTENT (질문 내용)
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	// CREATE_DATE (게시 날짜)
	@Column
	private LocalDateTime createDate;

	// LAST_MODIFIED (마지막 수정날짜)
	@Column
	private LocalDateTime lastModified;

	/*
	 * 해당 게시글에 달린 답변 리스트
	 * 
	 * 이 필드는 데이터베이스에 직접 컬럼으로 생성되지 않습니다. 대신, Answer 테이블에서 외래 키(question_id)를 통해
	 * Question 엔티티와 연결됩니다.
	 * 
	 * 새로운 Answer를 추가할 때, Question의 answerList에 자동으로 추가할 수 있음. AnswerService에서 새로운
	 * Answer를 만들 때, answer.setQuestion으로 Answer가 속한 Question을 매핑. 이때, question의
	 * answerList가 null이면, JPA는 자동으로 new ArrayList<>()를 통해 객체를 생성하고 해당 Answer 객체를
	 * 추가함.
	 */

	// Answer 엔터티에 있는 question 필드에 의해 매핑됨.
	// 즉, 질문-답변 간의 관계는 답변(Answer) 쪽에서 관리됨.
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList = new ArrayList<>();

	// QUESTION_VOTER라는 테이블이 생성됨. = [QUESTION_ID|VOTER_ID]
	@ManyToMany
	private Set<SiteUser> voter = new HashSet<>();
	
	// AUTHOR_ID (게시자 : 한 유저는 여려 질문 글을 작성할 수 있다)
	@ManyToOne
	private SiteUser author;

	public static Question createInstance(String subject, String content, SiteUser author) {
		return new Question().setQuestion(subject, content, author);
	}
	
	public Question setQuestion(String subject, String content, SiteUser author) {
		this.subject = subject;
		this.content = content;
		this.author = author;
		this.createDate = LocalDateTime.now();
		this.lastModified = LocalDateTime.now();
		
		return this;
	}
	
	public void modify(String subject, String content) {
		this.subject = subject;
		this.content = content;
		this.lastModified = LocalDateTime.now();
	}
	
	public void vote(SiteUser siteUser) {
		this.voter.add(siteUser);
	}
	
}
