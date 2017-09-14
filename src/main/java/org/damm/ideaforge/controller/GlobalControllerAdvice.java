package org.damm.ideaforge.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ModelAttribute("currentUser")
	public String currentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
