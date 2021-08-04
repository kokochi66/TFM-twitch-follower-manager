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
		String errMsg = "";
		
		if(exception instanceof UsernameNotFoundException) errMsg = "?errMsg=UsernameNotFound";
		if(exception instanceof BadCredentialsException) errMsg = "?errMsg=BadCredential";
		if(exception instanceof LockedException) errMsg = "?errMsg=isLocked";
		if(exception instanceof DisabledException) errMsg = "?errMsg=isDisabled";
		if(exception instanceof AccountExpiredException) errMsg = "?errMsg=AccountExpired";
		if(exception instanceof CredentialsExpiredException) errMsg = "?errMsg=CredentialsExpired";
		
		response.sendRedirect(defaultFailureUrl+errMsg);
	}

}
