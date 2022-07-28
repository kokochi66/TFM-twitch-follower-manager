package com.kokochi.samp.mapper;

import com.kokochi.samp.domain.ClipTwitchShortsBanVO;
import com.kokochi.samp.domain.ClipTwitchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClipTwitchShortsBanMapper {
    ClipTwitchShortsBanVO readClipTwitchShortsBan(String id) throws Exception;
    List<ClipTwitchShortsBanVO> readClipTwitchShortsBanList(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception;
    void createClipTwitchShortsBan(ClipTwitchShortsBanVO clipTwitchShortsBanVO) throws Exception;
    void deleteClipTwitchShortsBanById(String id) throws Exception;
    void deleteClipTwitchShortsBanByUserId(String userId) throws Exception;
}
