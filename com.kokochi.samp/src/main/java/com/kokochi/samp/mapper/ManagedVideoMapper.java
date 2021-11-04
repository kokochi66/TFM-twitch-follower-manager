package com.kokochi.samp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kokochi.samp.domain.ManagedVideoVO;

public interface ManagedVideoMapper {
	
	public ManagedVideoVO isManagedVideo(ManagedVideoVO mVideo) throws Exception;
	
	public void addManageVideo(ManagedVideoVO mVideo) throws Exception;
	
	public void deleteManageVideo(ManagedVideoVO mVideo) throws Exception;
	
	public List<ManagedVideoVO> listVideo(String user_id) throws Exception;
	
	public List<ManagedVideoVO> list_numVideo(@Param("user_id")String user_id, @Param("left")int left, @Param("right")int right) throws Exception;
}
