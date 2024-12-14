package com.mysite.sbb;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.user.SiteUser;

public abstract class BaseAuthorityValidator {

	protected void validateUserAuthority(SiteUser author, Principal principal, HttpStatus httpStatus, String message) {

		if (!author.getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(httpStatus, message);
		}
	}

}
