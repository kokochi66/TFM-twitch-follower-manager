package com.kokochi.samp.service.impl;

import com.kokochi.samp.domain.ClipTwitchShortsBanVO;
import com.kokochi.samp.mapper.ClipTwitchShortsBanMapper;
import com.kokochi.samp.service.ClipTwitchClipShortsBanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clipTwitchClipShortsBanService")
public class ClipTwitchClipShortsBanServiceImpl implements ClipTwitchClipShortsBanService {

    @Autowired
    private ClipTwitchShortsBanMapper clipTwitchShortsBanMapper;

    @Override
    public ClipTwitchShortsBanVO readClipTwitchShortsBan(String id) throws Exception {
        return clipTwitchShortsBanMapper.readClipTwitchShortsBan(id);
    }

    @Override
    public List<ClipTwitchShortsBanVO> readClipTwitchShortsBanList(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception {
        return clipTwitchShortsBanMapper.readClipTwitchShortsBanList(clipTwitchShortsBanVO);
    }

    @Override
    public void createClipTwitchShortsBan(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception {
        clipTwitchShortsBanMapper.createClipTwitchShortsBan(clipTwitchShortsBanVO);
    }

    @Override
    public void deleteClipTwitchShortsBanById(String id) throws Exception {
        clipTwitchShortsBanMapper.deleteClipTwitchShortsBanById(id);
    }

    @Override
    public void deleteClipTwitchShortsBanByUserId(String userId) throws Exception {
        clipTwitchShortsBanMapper.deleteClipTwitchShortsBanByUserId(userId);
    }
}
