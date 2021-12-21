package com.kokochi.samp.security;

import java.util.*;

import com.kokochi.samp.domain.*;
import com.kokochi.samp.mapper.UserFollowMapper;
import com.kokochi.samp.mapper.UserTwitchMapper;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.mapper.TwitchKeyMapper;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.innerProcess.PostQuery;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailService")
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
//		System.out.println("UserDetailService - loadUserByUsername :: 진입 :: " + user_id);
		try {
			MemberVO memberVO = mapper.readUserByUserId(user_id);
			List<MemberAuthVO> auth = mapper.readAuthList(memberVO.getId());
			UserDTO user = new UserDTO(memberVO, auth);
//			System.out.println("UserDetailService - loadUserByUsername :: 데이터 가져옴 :: " + user.getUser_id() +" " + user.getTwitch_user_id() +" " + auth.size());
			return user;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public UserDetails loadUserByTwitchUsername(String twitch_user_id) throws Exception {
//		System.out.println("UserDetailService - loadUserByTwitchUsername :: 진입 :: " + twitch_user_id);
		MemberVO memberVO = mapper.readUserByTwitchId(twitch_user_id);
		List<MemberAuthVO> auth = mapper.readAuthList(memberVO.getId());
		UserDTO user = new UserDTO(memberVO, auth);
//		System.out.println("UserDetailService - loadUserByUsername :: 데이터 가져옴 :: " + user.getUser_id() +" " + user.getTwitch_user_id() +" " + auth.size());
		PostQuery postQuery = new PostQuery();
		postQuery.initManagedFollow(user.getTwitch_user_id(), user.getUser_id());
		return user;
	}

}
