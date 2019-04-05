package org.damm.ideaforge.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.damm.ideaforge.pojo.User;
import org.damm.ideaforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class OAuthUserFilter implements Filter {

	@Autowired
	private UserService userService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			User existingUser = userService.findUserByEmail(userName);
			if (existingUser == null) {
				User user = new User();
				user.setEmail(userName);
				user.setName(userName);
				userService.saveUser(user);
			}
		}
		chain.doFilter(request, response);
	}

}
