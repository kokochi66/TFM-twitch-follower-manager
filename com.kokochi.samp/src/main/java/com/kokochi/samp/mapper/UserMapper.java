package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.MemberAuthVO;

public interface UserMapper {
	public MemberVO readUser(String user_id) throws Exception;
	
	public MemberVO readUserByTwitchId(String twitch_user_id) throws Exception;
	
	public List<MemberAuthVO> readAuth(String user_id) throws Exception;
	
	public void create(MemberVO memberVO) throws Exception;
	
	public void delete(String user_id) throws Exception;
	
	public void update(MemberVO memberVO) throws Exception;
	
	public List<MemberVO> userList() throws Exception;
	
	public void addAuth(MemberAuthVO auth) throws Exception;
	
	public void delAuth(MemberAuthVO auth) throws Exception;
}
