package com.mysite.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
	
	@NotEmpty(message = "제목은 필수인데...")
	@Size(max=200, message = "제목이 너무 길어요.. (최대 200자)")
	private String subject;
	
	@NotEmpty(message = "내용은 필수인데...")
	private String content;
	
}
