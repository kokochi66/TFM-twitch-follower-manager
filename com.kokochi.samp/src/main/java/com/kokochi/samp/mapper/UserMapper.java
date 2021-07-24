package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;

public interface UserMapper {
	public Member readUser(String user_id) throws Exception;
	
	public List<MemberAuth> readAuth(String user_id) throws Exception;
}
