package com.kokochi.samp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.domain.ManagedVideoVO;
import com.kokochi.samp.mapper.ManagedFollowMapper;
import com.kokochi.samp.mapper.ManagedVideoMapper;

@Service
public class ManagedService {
	
	@Autowired
	ManagedFollowMapper followMapper;
	
	@Autowired
	ManagedVideoMapper videoMapper;
	
	public boolean isManagedFollow(ManagedFollowVO managed) throws Exception {
		ManagedFollowVO result = followMapper.selectManaged(managed);
		return result != null;
	}

	public ManagedFollowVO getManagedFollow(ManagedFollowVO managed) throws Exception {
		return followMapper.selectManaged(managed);
	}
	
	public void createFollow(ManagedFollowVO managed) throws Exception {
		followMapper.addManage(managed);
	}
	
	public void removeFollow(String id) throws Exception {
		followMapper.deleteManage(id);
	}
	
	public List<ManagedFollowVO> listFollow(String user_id) throws Exception {
		return followMapper.selectManagedListByUserId(user_id);
	}
	
	public List<ManagedFollowVO> list_numFollow(String user_id, int left, int right) throws Exception {
		return followMapper.selectManagedListByUserId(user_id);
	}
	
	
	public boolean isManagedVideo(ManagedVideoVO mVideo) throws Exception {
		ManagedVideoVO result = videoMapper.isManagedVideo(mVideo);
		return result != null;
	}
	
	public void createVideo(ManagedVideoVO mVideo) throws Exception {
		videoMapper.addManageVideo(mVideo);
	}
	
	public void removeVideo(ManagedVideoVO mVideo) throws Exception {
		videoMapper.deleteManageVideo(mVideo);
	}
	
	public List<ManagedVideoVO> listVideo(String user_id) throws Exception {
		return videoMapper.listVideo(user_id);
	}
	
	public List<ManagedVideoVO> list_numVideo(String user_id, int left, int right) throws Exception {
		return videoMapper.list_numVideo(user_id, left, right);
	}
}
