package com.mysite.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private UserService userService;

	@Test
	void testJpa() {

		this.userService.create("Anna", "2848", "fksd@fdf");
		
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			SiteUser u = userService.getUser("Anna");
			this.questionService.create(subject, content, u);
		}

		insertionQuestionData();
		insertionAnswerData();
	}

	void insertionQuestionData() {
		generationQuestionData("나 8비 항전 화력제어반인데", "ㅅ매일매일 ㅈ뺑이친다 하", "Anna");
		generationQuestionData("수ㅌ능등급 76565인데 서성한 ㄱㄴ?", "서강대교 성수대교 한강대교", "Anna");
		generationQuestionData("여자친구 내년 1월 재결합 한다는데", "지리누 ㅋㅋㅋㅋㅋ", "Anna");
		generationQuestionData("국군의 날 쉰다고 좋댄다 ㅋㅋㅋㅋ", "당연히 쉬는게 맞는거 아님? 국방부식 가스라이팅에 속지마라 ㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "Anna");
		generationQuestionData("삐에스타", "내맘에 태양을 꾹 삼킨채", "Anna");
		generationQuestionData("지금 이 시점 짬찌들 이 짬주면 갖냐?", "36%", "Anna");
		generationQuestionData("나 8비 화력제어반인데", "매일매일 ㅈ뺑이친다 하", "Anna");
		generationQuestionData("공군가서 꿀빨려는데 무슨 특기가 좋음?", "ㅈㄱㄴ", "Anna");
		generationQuestionData("대 855기", "오늘자 복무율 36퍼 달성 ㅅㅅ", "Anna");
		generationQuestionData("컴공오지마라", "별다른 재능 없으면 인생 조지는 지름길이다. 오지말라했다", "Anna");
	}

	void insertionAnswerData() {
		generationAnswerData(308, "ㅇㅇ 특히 20비 기무탄이 꿀임", "Anna");
		generationAnswerData(308, "ㄴ-> 이새낀 진짜 악마네 ㅋㅋㅋㅋ", "Anna");
		generationAnswerData(308, "그냥 헌급방이나 가라", "Anna");
		generationAnswerData(308, "나 8비 항전 화력반인데 매일매일 ㅈ뻉이친다", "Anna");
		generationAnswerData(308, "그냥 닥치고 육군이나 가라 유꾼 아미 타이거! 유꾼유꾼유꾼ㅇ!", "Anna");
		generationAnswerData(308, "병신들ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ", "Anna");
	}
	
	void generationQuestionData(String subject, String content, String userName) {
		SiteUser u = this.userService.getUser(userName);
		questionService.create(subject, content, u);
	}

	void generationAnswerData(Integer questionID, String content, String userName) {
		Question question = this.questionService.getQuestion(questionID);
		SiteUser user = this.userService.getUser(userName);
		this.answerService.create(question, content, user);
	}

}