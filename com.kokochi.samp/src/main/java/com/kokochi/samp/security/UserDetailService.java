package com.kokochi.samp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;
import com.kokochi.samp.domain.TwitchKey;
import com.kokochi.samp.mapper.TwitchKeyMapper;
import com.kokochi.samp.mapper.UserMapper;

@Service
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private TwitchKeyMapper keyMapper;
	
	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
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
	
	public UserDetails loadUserByTwitchUsername(String twitch_user_id) throws Exception {
		
		Member member = mapper.readUserByTwitchId(twitch_user_id);
		List<MemberAuth> auth = mapper.readAuth(member.getUser_id());
		UserDTO user = new UserDTO(member, auth);
		return user;
	}
	
	public void userRegister(Member member) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		member.setUser_pwd(encoder.encode(member.getUser_pwd()));
		mapper.create(member);
		
		MemberAuth auth = new MemberAuth();
		auth.setUser_id(member.getUser_id());
		auth.setAuthority("ROLE_MEMBER");
		addAuth(auth);
	}
	
	public void userDelete(String user_id) throws Exception {
		mapper.delete(user_id);
	}
	
	public void userUpdate(Member member) throws Exception {
		mapper.update(member);
	}
	
	public List<Member> userList() throws Exception {
		return mapper.userList();
	}
	
	public void addAuth(MemberAuth auth) throws Exception {
		mapper.addAuth(auth);
	}
	
	public void delAuth(MemberAuth auth) throws Exception {
		mapper.delAuth(auth);
	}
	
	public TwitchKey getKey(String key_name) throws Exception {
		return keyMapper.read(key_name);
	}
	
	

}
