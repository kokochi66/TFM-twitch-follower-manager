package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.MemberAuthVO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;

@Mapper
public interface UserMapper {
	public MemberVO readUser(String id) throws Exception;

	public MemberVO readUserByUserId(String user_id) throws Exception;

	public MemberVO readUserByTwitchId(String twitch_user_id) throws Exception;
	
	public void create(MemberVO memberVO) throws Exception;
	
	public void delete(String user_id) throws Exception;
	
	public void update(MemberVO memberVO) throws Exception;
	
	public List<MemberVO> userList() throws Exception;

	public List<MemberAuthVO> readAuthList(String user_id) throws Exception;
	
	public void addAuth(MemberAuthVO auth) throws Exception;
	
	public void delAuth(MemberAuthVO auth) throws Exception;
}
