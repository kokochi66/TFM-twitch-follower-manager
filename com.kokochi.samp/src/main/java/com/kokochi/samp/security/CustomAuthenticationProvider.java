package com.kokochi.samp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kokochi.samp.DTO.UserDTO;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDetailsService service;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		
		
		String user_id = (String) authentication.getPrincipal();
		String user_pwd = (String) authentication.getCredentials();
		UsernamePasswordAuthenticationToken authToken = null;
		
		if(user_id.equals("OAuth2_authentication")) {
			// OAuth2 토큰이 들어온 경우의 로그인 처리 (로그인 된 것으로 처리해야함)
			// user_id가 OAuth2 토큰이라면 user_pwd가 사용자 계정이 됨.
			UserDTO user = (UserDTO) service.loadUserByUsername(user_pwd);
			user.setUser_pwd("");	// 비밀번호는 객체에 적용하지 않음
			
			Object returnUser = user;
			authToken = new UsernamePasswordAuthenticationToken(returnUser, user_pwd);
			
		} else {
			// 일반 로그인 처리
			UserDTO user = (UserDTO) service.loadUserByUsername(user_id);
			
			if(user == null) {
				System.out.println("CustomAuthenticationProvider - 사용자가 널값임");
				throw new UsernameNotFoundException(user_id);
			}
			if(!passwordEncoder.matches(user_pwd, user.getPassword())) throw new BadCredentialsException(user_id);
//			if(user.isEnabled()) throw new DisabledException(user_id);
			
//			LoginProcess(user_id, user_pwd, user);	// 로그인 처리를 별도 메소드로 함
			user.setUser_pwd("");	// 비밀번호는 객체에 적용하지 않음
			
			Object returnUser = user;
			authToken = new UsernamePasswordAuthenticationToken(returnUser, user_pwd);
			
		}
		
		System.out.println(" CustomAuthenticationProvider - 결과값은 반환되었음");
		
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private boolean matchPassword(String loginPwd, String user_pwd) {
		return loginPwd.equals(user_pwd);
	}
	
	public void LoginProcess(String user_id, String user_pwd, UserDTO user) {
		

		
	}

}
