package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.ClipTwitchVO;
import com.kokochi.samp.domain.VideoTwitchVO;

import java.util.List;


public interface ClipTwitchMapper {
    public ClipTwitchVO read(ClipTwitchVO clipTwitchVO) throws Exception;
    public List<ClipTwitchVO> readList(ClipTwitchVO clipTwitchVO) throws Exception;
    public List<ClipTwitchVO> readRecentFollowList(ClipTwitchVO clipTwitchVO) throws Exception;
    public void create(ClipTwitchVO clipTwitchVO) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteByUserId(String user_id) throws Exception;
    public void update(ClipTwitchVO clipTwitchVO) throws Exception;
}
