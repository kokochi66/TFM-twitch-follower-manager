package com.kokochi.samp.service.impl;

import com.kokochi.samp.domain.MemberAuthVO;
import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.VideoTwitchVO;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.mapper.VideoTwitchMapper;
import com.kokochi.samp.service.UserService;
import com.kokochi.samp.service.VideoTwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("videoTwitchService")
public class VideoTwitchServiceImpl implements VideoTwitchService {

    @Autowired
    VideoTwitchMapper videoTwitchMapper;

    @Override
    public VideoTwitchVO read(VideoTwitchVO videoTwitchVO) throws Exception {
        return videoTwitchMapper.read(videoTwitchVO);
    }
    @Override
    public List<VideoTwitchVO> readList(VideoTwitchVO videoTwitchVO) throws Exception {
        return videoTwitchMapper.readList(videoTwitchVO);
    }
    @Override
    public void create(VideoTwitchVO videoTwitchVO) throws Exception {
        videoTwitchMapper.create(videoTwitchVO);
    }
    @Override
    public void deleteById(String id) throws Exception {
        videoTwitchMapper.deleteById(id);
    }
    @Override
    public void deleteByUserId(String user_id) throws Exception {
        videoTwitchMapper.deleteByUserId(user_id);
    }
    @Override
    public void update(VideoTwitchVO videoTwitchVO) throws Exception {
        videoTwitchMapper.update(videoTwitchVO);
    }
}
