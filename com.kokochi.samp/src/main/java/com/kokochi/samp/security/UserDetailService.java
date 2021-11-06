package com.kokochi.samp.security;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.TwitchKeyVO;
import com.kokochi.samp.mapper.TwitchKeyMapper;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.innerProcess.PostQuery;

@Service("userDetailService")
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private TwitchKeyMapper keyMapper;
	
	@Override
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		System.out.println("UserDetailService - loadUserByUsername :: 진입 :: " + user_id);
		try {
			MemberVO memberVO = mapper.readUserByUserId(user_id);
			List<MemberAuthVO> auth = mapper.readAuthList(user_id);
			UserDTO user = new UserDTO(memberVO, auth);
			System.out.println("UserDetailService - loadUserByUsername :: 데이터 가져옴 :: " + user.getUser_id() +" " + user.getTwitch_user_id() +" " + auth.size());
			return user;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public UserDetails loadUserByTwitchUsername(String twitch_user_id) throws Exception {
		System.out.println("UserDetailService - loadUserByTwitchUsername :: 진입 :: " + twitch_user_id);
		MemberVO memberVO = mapper.readUserByTwitchId(twitch_user_id);
		List<MemberAuthVO> auth = mapper.readAuthList(memberVO.getUser_id());
		UserDTO user = new UserDTO(memberVO, auth);
		System.out.println("UserDetailService - loadUserByUsername :: 데이터 가져옴 :: " + user.getUser_id() +" " + user.getTwitch_user_id() +" " + auth.size());
		PostQuery postQuery = new PostQuery();
		postQuery.initManagedFollow(user.getTwitch_user_id(), user.getUser_id());
		return user;
	}
	
	public void userRegister(MemberVO memberVO) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		memberVO.setUser_pwd(encoder.encode(memberVO.getUser_pwd()));
		mapper.create(memberVO);
		
		MemberAuthVO auth = new MemberAuthVO();
		auth.setId(UUID.randomUUID().toString());
		auth.setUser_id(memberVO.getId());
		auth.setAuthority("ROLE_MEMBER");
		addAuth(auth);
	}
	
	public void userDelete(String user_id) throws Exception {
		mapper.delete(user_id);
	}
	
	public void userUpdate(MemberVO memberVO) throws Exception {
		mapper.update(memberVO);
	}
	
	public List<MemberVO> userList() throws Exception {
		return mapper.userList();
	}
	
	public void addAuth(MemberAuthVO auth) throws Exception {
		mapper.addAuth(auth);
	}
	
	public void delAuth(MemberAuthVO auth) throws Exception {
		mapper.delAuth(auth);
	}
	
	public TwitchKeyVO getKey(String key_name) throws Exception {
		return keyMapper.read(key_name);
	}
	
	

}
