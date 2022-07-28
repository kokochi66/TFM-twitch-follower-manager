package com.kokochi.samp.service.impl;

import com.kokochi.samp.domain.ClipTwitchVO;
import com.kokochi.samp.domain.VideoTwitchVO;
import com.kokochi.samp.mapper.ClipTwitchMapper;
import com.kokochi.samp.mapper.VideoTwitchMapper;
import com.kokochi.samp.service.ClipTwitchService;
import com.kokochi.samp.service.VideoTwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clipTwitchService")
public class ClipTwitchServiceImpl implements ClipTwitchService {

    @Autowired
    ClipTwitchMapper clipTwitchMapper;


    @Override
    public ClipTwitchVO read(ClipTwitchVO clipTwitchVO) throws Exception {
        return clipTwitchMapper.read(clipTwitchVO);
    }

    @Override
    public List<ClipTwitchVO> readList(ClipTwitchVO clipTwitchVO) throws Exception {
        return clipTwitchMapper.readList(clipTwitchVO);
    }

    @Override
    public List<ClipTwitchVO> readRecentFollowList(ClipTwitchVO clipTwitchVO) throws Exception {
        return clipTwitchMapper.readRecentFollowList(clipTwitchVO);
    }

    @Override
    public void create(ClipTwitchVO clipTwitchVO) throws Exception {
        clipTwitchMapper.create(clipTwitchVO);
    }

    @Override
    public void createList(List<ClipTwitchVO> list) throws Exception {
        clipTwitchMapper.createList(list);
    }

    @Override
    public void deleteById(String id) throws Exception {
        clipTwitchMapper.deleteById(id);
    }

    @Override
    public void deleteByUserId(String user_id) throws Exception {
        clipTwitchMapper.deleteByUserId(user_id);
    }

    @Override
    public void deleteList(String ids) throws Exception {
        clipTwitchMapper.deleteList(ids);
    }

    @Override
    public void update(ClipTwitchVO clipTwitchVO) throws Exception {
        clipTwitchMapper.update(clipTwitchVO);
    }
}
