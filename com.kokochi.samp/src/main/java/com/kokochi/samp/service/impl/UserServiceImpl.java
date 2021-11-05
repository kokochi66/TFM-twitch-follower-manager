package com.kokochi.samp.service.impl;

import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public MemberVO readUser(String user_id) throws Exception {
        return mapper.readUser(user_id);
    }

    @Override
    public MemberVO readUserByTwitchId(String twitch_user_id) throws Exception {
        return mapper.readUserByTwitchId(twitch_user_id);
    }

    @Override
    public List<MemberAuthVO> readAuth(String user_id) throws Exception {
        return mapper.readAuth(user_id);
    }

    @Override
    public void create(MemberVO memberVO) throws Exception {
        mapper.create(memberVO);
    }

    @Override
    public void delete(String user_id) throws Exception {
        mapper.delete(user_id);
    }

    @Override
    public void update(MemberVO memberVO) throws Exception {
        mapper.update(memberVO);
    }

    @Override
    public List<MemberVO> userList() throws Exception {
        return mapper.userList();
    }

    @Override
    public void addAuth(MemberAuthVO auth) throws Exception {
        mapper.addAuth(auth);
    }

    @Override
    public void delAuth(MemberAuthVO auth) throws Exception {
        mapper.delAuth(auth);
    }
}
