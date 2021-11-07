package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.domain.UserTwitchVO;

import java.util.List;

public interface UserFollowMapper {
    public UserFollowVO read(UserFollowVO userFollowVO) throws Exception;
    public List<UserFollowVO> readList(UserFollowVO userFollowVO) throws Exception;
    public void create(UserFollowVO userFollowVO) throws Exception;
    public void deleteById(String id) throws Exception;
}
