package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.UserTwitchVO;
import com.kokochi.samp.domain.VideoTwitchVO;

import java.util.List;


public interface VideoTwitchMapper {
    public VideoTwitchVO read(VideoTwitchVO videoTwitchVO) throws Exception;
    public List<VideoTwitchVO> readList(VideoTwitchVO videoTwitchVO) throws Exception;
    public List<VideoTwitchVO> readRecentFollowList(VideoTwitchVO videoTwitchVO) throws Exception;
    public void create(VideoTwitchVO videoTwitchVO) throws Exception;
    public void createList(List<VideoTwitchVO> list) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteList(String ids) throws Exception;
    public void deleteByUserId(String user_id) throws Exception;
    public void update(VideoTwitchVO videoTwitchVO) throws Exception;
}
