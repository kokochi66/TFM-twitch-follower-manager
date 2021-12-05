package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.UserTwitchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserTwitchMapper {
    public UserTwitchVO read(UserTwitchVO userTwitchVO) throws Exception;
    public List<UserTwitchVO> readList(UserTwitchVO userTwitchVO) throws Exception;
    public List<UserTwitchVO> readFollowList(String from_id) throws Exception;
    public List<UserTwitchVO> readManagedList(String user_id) throws Exception;
    public void create(UserTwitchVO userTwitchVO) throws Exception;
    public void createList(List<UserTwitchVO> list) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteByLogin(String login) throws Exception;
    public void update(UserTwitchVO userTwitchVO) throws Exception;
}
