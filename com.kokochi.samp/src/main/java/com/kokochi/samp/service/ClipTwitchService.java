package com.kokochi.samp.service;

import com.kokochi.samp.domain.ClipTwitchVO;
import com.kokochi.samp.domain.VideoTwitchVO;

import java.util.List;


public interface ClipTwitchService {
    public ClipTwitchVO read(ClipTwitchVO clipTwitchVO) throws Exception;
    public List<ClipTwitchVO> readList(ClipTwitchVO clipTwitchVO) throws Exception;
    public List<ClipTwitchVO> readRecentFollowList(ClipTwitchVO clipTwitchVO) throws Exception;
    public void create(ClipTwitchVO clipTwitchVO) throws Exception;
    public void createList(List<ClipTwitchVO> list) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteList(String ids) throws Exception;
    public void deleteByUserId(String user_id) throws Exception;
    public void update(ClipTwitchVO clipTwitchVO) throws Exception;
}
