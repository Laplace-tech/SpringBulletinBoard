package com.mysite.sbb.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class SiteUser {

	// ID : SiteUser의 기본키
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// USER_NAME : 유저의 이름
	@Column(unique = true)
	private String userName;

	// PASSWORD : 유저의 비밀번호
	private String password;
	
	// EMAIL : 유저의 이메일주소
	@Column(unique = true)
	private String email;
	
	public static SiteUser createInstance(String userName, String encodedPassword, String email) {
		return new SiteUser().setUser(userName, encodedPassword, email);
	}
	
	public SiteUser setUser(String userName, String encodedPassword, String email) {
		this.userName = userName;
		this.password = encodedPassword;
		this.email = email;
		
		return this;
	}
	
	
	
}
