package com.kokochi.samp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.domain.ManagedVideo;

public interface ManagedVideoMapper {
	
	public ManagedVideo isManagedVideo(ManagedVideo mVideo) throws Exception;
	
	public void addManageVideo(ManagedVideo mVideo) throws Exception;
	
	public void deleteManageVideo(ManagedVideo mVideo) throws Exception;
	
	public List<ManagedVideo> listVideo(String user_id) throws Exception;
	
	public List<ManagedVideo> list_numVideo(@Param("user_id")String user_id, @Param("left")int left, @Param("right")int right) throws Exception;
}
