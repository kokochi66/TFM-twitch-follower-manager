package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.domain.UserTwitchVO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;
import java.util.List;

@Mapper
public interface UserFollowMapper {
    public UserFollowVO read(UserFollowVO userFollowVO) throws Exception;
    public List<UserFollowVO> readList(UserFollowVO userFollowVO) throws Exception;
    public void create(UserFollowVO userFollowVO) throws Exception;
    public void createList(List<UserFollowVO> list) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteList(String ids) throws Exception;
}
