package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;

public interface UserMapper {
	public Member readUser(String user_id) throws Exception;
	
	public Member readUserByTwitchId(String twitch_user_id) throws Exception;
	
	public List<MemberAuth> readAuth(String user_id) throws Exception;
	
	public void create(Member member) throws Exception;
	
	public void delete(String user_id) throws Exception;
	
	public void update(Member member) throws Exception;
	
	public List<Member> userList() throws Exception;
	
	public void addAuth(MemberAuth auth) throws Exception;
	
	public void delAuth(MemberAuth auth) throws Exception;
}
