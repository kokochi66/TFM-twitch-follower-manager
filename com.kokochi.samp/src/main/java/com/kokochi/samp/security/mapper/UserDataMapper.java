package com.kokochi.samp.security.mapper;

import java.util.List;

import com.kokochi.samp.domain.Member;

public interface UserDataMapper {
	public void create(Member member) throws Exception;
	
	public Member member(String user_id) throws Exception;
	
	public void update(Member member) throws Exception;
	
	public void delete(String user_id) throws Exception;
	
	public List<Member> list() throws Exception;
}
