package com.kokochi.samp.security;

import com.kokochi.samp.DTO.Key;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.queryAPI.GetFollow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDetailService service;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
//		System.out.println("CustomAuthenticationProvider - authenticate :: 진입");
		
		String user_id = (String) authentication.getPrincipal();
		String user_pwd = (String) authentication.getCredentials();
		UsernamePasswordAuthenticationToken authToken = null;
//		System.out.println("CustomAuthenticationProvider - authenticate :: " + user_id + " " + user_pwd);
		
		if(user_id.equals("OAuth2_authentication")) {
			// OAuth2 토큰이 들어온 경우의 로그인 처리 (로그인 된 것으로 처리해야함)
			// user_id가 OAuth2_authentication이라면 user_pwd가 OAuth2 토큰이 들어오게됨.
			// 들어온 토큰으로 API처리를 이용해서 사용자 정보를 가져와서, 이를 이용해서 Service에서 값을 가져옴
			GetStream streamGenerator = new GetStream();
			GetFollow followGetter = new GetFollow();
			try {
				String client_id = new Key().getClientId();
				TwitchUser tuser = streamGenerator.getUser(client_id, user_pwd, "");
				UserDTO user = (UserDTO) service.loadUserByTwitchUsername(tuser.getId());
				if(user == null) throw new UsernameNotFoundException(user_id);
				user.setUser_pwd("");	// 비밀번호는 객체에 적용하지 않음
				user.setOauth_token(user_pwd);

				// 팔로우 리스트 가져와서 갱신하기
				UserFollowVO userFollowVO = new UserFollowVO();
				userFollowVO.setFrom_id(tuser.getId());
				ArrayList<UserFollowVO> allFollowedList = followGetter.getAllFollowedListToFollowVO(client_id, user_pwd, "from_id=" + tuser.getId()); 	// 갱신할 팔로우 리스트
				List<UserFollowVO> userFollowVOS = service.readUserFollowList(userFollowVO);														// db의 팔로우 리스트
				HashSet<String> followSet = new HashSet<>();																						// db의 팔로우 리스트의 Set
				for (UserFollowVO c_userFollow : userFollowVOS) followSet.add(c_userFollow.getTo_id());
				for (UserFollowVO c_userFollowVO : allFollowedList) {
					// 갱신할 값이 이미 db에도 있으면 Set에서 값을 지운다.
					if(followSet.contains(c_userFollowVO.getTo_id())) followSet.remove(c_userFollowVO.getTo_id());
					service.addUserFollow(c_userFollowVO);
				}
				for (String s : followSet) service.deleteUserFollow(s);

				MemberVO memberVO = new MemberVO();
				memberVO.setUser_id(user.getUser_id());
				memberVO.setOAuth2_token(user_pwd);
				service.userUpdate(memberVO);

				authToken = new UsernamePasswordAuthenticationToken((Object) user, user_pwd, user.getAuthorities());
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

			authToken = new UsernamePasswordAuthenticationToken((Object) user, user_pwd, user.getAuthorities());
		}
		// 팔로우 데이터를 DB에 저장

		// 팔로우우

		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
