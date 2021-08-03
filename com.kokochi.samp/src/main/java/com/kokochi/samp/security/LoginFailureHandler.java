package com.kokochi.samp.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private String user_id;
	private String user_pwd;
	private String error_name;
	private String defaultFailureUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		if(exception instanceof UsernameNotFoundException) request.setAttribute("errMsg", "존재하지 않는 사용자입니다.");
		if(exception instanceof BadCredentialsException) request.setAttribute("errMsg", "아이디나 비밀번호가 틀립니다.");
		if(exception instanceof LockedException) request.setAttribute("errMsg", "정지당한 아이디입니다.");
		if(exception instanceof DisabledException) request.setAttribute("errMsg", "사용이 불가능한 아이디 입니다.");
		if(exception instanceof AccountExpiredException) request.setAttribute("errMsg", "만료된 아이디 입니다.");
		if(exception instanceof CredentialsExpiredException) request.setAttribute("errMsg", "비밀번호가 만료되었습니다.");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/auth/login");
		dispatcher.forward(request, response);
	}

}
