package com.kokochi.samp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.service.TwitchKeyService;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDetailService service;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		
		String user_id = (String) authentication.getPrincipal();
		String user_pwd = (String) authentication.getCredentials();
		UsernamePasswordAuthenticationToken authToken = null;
//		System.out.println("CustomAuthenticationProvider - authenticate :: " + user_id + " " + user_pwd);
		
		if(user_id.equals("OAuth2_authentication")) {
			// OAuth2 토큰이 들어온 경우의 로그인 처리 (로그인 된 것으로 처리해야함)
			// user_id가 OAuth2_authentication이라면 user_pwd가 OAuth2 토큰이 들어오게됨.
			// 들어온 토큰으로 API처리를 이용해서 사용자 정보를 가져와서, 이를 이용해서 Service에서 값을 가져옴
			GetStream streamGenerator = new GetStream();
			try {
				String client_id = service.getKey("client_id").getKey_value();
				
				TwitchUser tuser = streamGenerator.getUser(client_id, user_pwd, "");
				UserDTO user = (UserDTO) service.loadUserByTwitchUsername(tuser.getLogin());
				user.setUser_pwd("");	// 비밀번호는 객체에 적용하지 않음
				user.setOauth_token(user_pwd);
				
				Object returnUser = user;
				authToken = new UsernamePasswordAuthenticationToken(returnUser, user_pwd, user.getAuthorities());
//				System.out.println("CustomAuthenticationProvider - authenticated :: " + user.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		
			
		} else {
			// 일반 로그인 처리
			UserDTO user = (UserDTO) service.loadUserByUsername(user_id);
			
//			System.out.println("AuthController - authenticate :: enable = " + user.isEnabled());
			
			if(user == null) throw new UsernameNotFoundException(user_id);
			if(!passwordEncoder.matches(user_pwd, user.getPassword())) throw new BadCredentialsException(user_id);
			if(!user.isEnabled()) throw new DisabledException(user_id);
			
//			LoginProcess(user_id, user_pwd, user);	// 로그인 처리를 별도 메소드로 함
			user.setUser_pwd("");	// 비밀번호는 객체에 적용하지 않음
			
			Object returnUser = user;
			authToken = new UsernamePasswordAuthenticationToken(returnUser, user_pwd, user.getAuthorities());
			
		}
		
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
