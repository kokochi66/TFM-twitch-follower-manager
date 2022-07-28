package com.kokochi.samp.service;

import com.kokochi.samp.domain.ClipTwitchShortsBanVO;

import java.util.List;

public interface ClipTwitchClipShortsBanService {
    ClipTwitchShortsBanVO readClipTwitchShortsBan(String id) throws Exception;
    List<ClipTwitchShortsBanVO> readClipTwitchShortsBanList(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception;
    void createClipTwitchShortsBan(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception;
    void deleteClipTwitchShortsBanById(String id) throws Exception;
    void deleteClipTwitchShortsBanByUserId(String userId) throws Exception;
}
