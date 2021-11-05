package com.kokochi.samp.service;

import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.MemberVO;

import java.util.List;

public interface UserService {
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
