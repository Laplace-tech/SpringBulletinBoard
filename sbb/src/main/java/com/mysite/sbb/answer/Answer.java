package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Answer {

	// ID: Answer 객체의 고유번호(기본키)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// AUTHOR_ID: 작성자 ID (SiteUser 객체의 기본키)
	@ManyToOne
	private SiteUser author;

	// CONTENT: 답변 내용
	@Column(columnDefinition = "TEXT")
	private String content;

	// CREATE_DATE: 만든 날짜
	@Column
	private LocalDateTime createDate;

	// LAST_MODIFIED: 마지막 수정 날짜 및 시간
	@Column
	private LocalDateTime lastModified;

	// QUESTION_ID: 댓글 단 질문글을 참조하는 ID (Question 객체의 기본키)
	@ManyToOne
	private Question question;

	// ANSWER_VOTER: 여러 사용자가 여러 답변에 투표할 수 있음.
	@ManyToMany
	private Set<SiteUser> voter = new HashSet<>();
	
	
	public static Answer createInstance(Question question, String content, SiteUser author) {
		return new Answer().setAnswer(question, content, author);
	}
	
	public Answer setAnswer(Question question, String content, SiteUser author) {
        this.content = content;    
        this.question = question;
        this.author = author;
        this.createDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
		
        return this;
    }

	public void modify(String content) {
		this.content = content;
		this.lastModified = LocalDateTime.now();
	}

	public void vote(SiteUser siteUser) {
		this.voter.add(siteUser);
	}
	
	
}
