package com.kokochi.samp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;
import com.kokochi.samp.mapper.UserMapper;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("UserDetailService - loadUserByUsername 메소드 실행 " + user_id );
		try {
			Member member = mapper.readUser(user_id);
			List<MemberAuth> auth = mapper.readAuth(user_id);
			UserDTO user = new UserDTO(member, auth);
			
			return user;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	

}
