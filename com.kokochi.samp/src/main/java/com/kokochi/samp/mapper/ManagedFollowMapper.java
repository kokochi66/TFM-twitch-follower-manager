package com.kokochi.samp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kokochi.samp.domain.ManagedFollowVO;

import javax.annotation.Resource;

public interface ManagedFollowMapper {
	
	public ManagedFollowVO selectManaged(ManagedFollowVO managed) throws Exception;
	
	public void addManage(ManagedFollowVO managed) throws Exception;
	
	public void deleteManage(String id) throws Exception;
	
	public List<ManagedFollowVO> selectManagedListByUserId(String user_id) throws Exception;
	
//	public List<ManagedFollowVO> list_num(@Param("user_id")String user_id, @Param("left")int left, @Param("right")int right) throws Exception;
}
