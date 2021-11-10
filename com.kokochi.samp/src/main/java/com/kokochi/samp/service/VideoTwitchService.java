package com.kokochi.samp.service;

import com.kokochi.samp.domain.VideoTwitchVO;

import java.util.List;

public interface VideoTwitchService {
    public VideoTwitchVO read(VideoTwitchVO videoTwitchVO) throws Exception;
    public List<VideoTwitchVO> readList(VideoTwitchVO videoTwitchVO) throws Exception;
    public void create(VideoTwitchVO videoTwitchVO) throws Exception;
    public void deleteById(String id) throws Exception;
    public void deleteByUserId(String user_id) throws Exception;
    public void update(VideoTwitchVO videoTwitchVO) throws Exception;
}
