package com.kokochi.samp.service;

import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.domain.UserTwitchVO;

import java.util.List;

public interface UserService {
    MemberVO readUser(String user_id) throws Exception;

    MemberVO readUserByTwitchId(String twitch_user_id) throws Exception;

    List<MemberAuthVO> readAuth(String user_id) throws Exception;

    void create(MemberVO memberVO) throws Exception;

    void delete(String user_id) throws Exception;

    void update(MemberVO memberVO) throws Exception;

    List<MemberVO> userList() throws Exception;

    void addAuth(MemberAuthVO auth) throws Exception;

    void delAuth(MemberAuthVO auth) throws Exception;


    void addUserFollow(UserFollowVO userFollowVO) throws Exception;

    void addUserFollowList(List<UserFollowVO> list) throws Exception;

    void userRegister(MemberVO memberVO) throws Exception;

    void userUpdate(MemberVO memberVO) throws Exception;

    List<UserTwitchVO> readUserTwitchList(UserTwitchVO userTwitchVO) throws Exception;

    List<UserTwitchVO> readUserTwitchFollowList(String from_id) throws Exception;

    void addUserTwitch(UserTwitchVO userTwitchVO) throws Exception;

    List<UserFollowVO> readUserFollowList(UserFollowVO userFollowVO) throws Exception;

    void deleteUserFollow(String id) throws Exception;

    void deleteUserFollowList(List<String> list) throws Exception;
}
