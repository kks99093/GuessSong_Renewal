package com.guess.song.handller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.guess.song.service.UserService;

public class LoginFailUreHandller implements AuthenticationFailureHandler{
	
	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String result = userService.loginFail(request.getParameter("username"));
		
		request.setAttribute("loginFailed", result);
		request.getRequestDispatcher("/login").forward(request, response);
		
	}
	

}
