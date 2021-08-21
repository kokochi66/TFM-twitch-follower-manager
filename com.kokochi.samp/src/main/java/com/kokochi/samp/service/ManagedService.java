package com.kokochi.samp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.domain.ManagedVideo;
import com.kokochi.samp.mapper.ManagedFollowMapper;
import com.kokochi.samp.mapper.ManagedVideoMapper;

@Service
public class ManagedService {
	
	@Autowired
	ManagedFollowMapper FollowMapper;
	
	@Autowired
	ManagedVideoMapper VideoMapper;
	
	public boolean isManagedFollow(ManagedFollow managed) throws Exception {
		ManagedFollow result = FollowMapper.isManaged(managed);
		return result != null;
	}
	
	public void createFollow(ManagedFollow managed) throws Exception {
		FollowMapper.addManage(managed);
	}
	
	public void removeFollow(ManagedFollow managed) throws Exception {
		FollowMapper.deleteManage(managed);
	}
	
	public List<ManagedFollow> listFollow(String user_id) throws Exception {
		return FollowMapper.list(user_id);
	}
	
	public List<ManagedFollow> list_numFollow(String user_id, int left, int right) throws Exception {
		return FollowMapper.list_num(user_id, left, right);
	}
	
	
	public boolean isManagedVideo(ManagedVideo mVideo) throws Exception {
		ManagedVideo result = VideoMapper.isManagedVideo(mVideo);
		return result != null;
	}
	
	public void createVideo(ManagedVideo mVideo) throws Exception {
		VideoMapper.addManageVideo(mVideo);
	}
	
	public void removeVideo(ManagedVideo mVideo) throws Exception {
		VideoMapper.deleteManageVideo(mVideo);
	}
	
	public List<ManagedVideo> listVideo(String user_id) throws Exception {
		return VideoMapper.listVideo(user_id);
	}
	
	public List<ManagedVideo> list_numVideo(String user_id, int left, int right) throws Exception {
		return VideoMapper.list_numVideo(user_id, left, right);
	}
}
