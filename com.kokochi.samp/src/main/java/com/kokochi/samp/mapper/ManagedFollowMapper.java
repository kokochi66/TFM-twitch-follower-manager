package com.kokochi.samp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kokochi.samp.domain.ManagedFollow;

public interface ManagedFollowMapper {
	
	public ManagedFollow isManaged(ManagedFollow managed) throws Exception;
	
	public void addManage(ManagedFollow managed) throws Exception;
	
	public void deleteManage(ManagedFollow managed) throws Exception;
	
	public List<ManagedFollow> list(String user_id) throws Exception;
	
	public List<ManagedFollow> list_num(@Param("user_id")String user_id, @Param("left")int left, @Param("right")int right) throws Exception;
}
