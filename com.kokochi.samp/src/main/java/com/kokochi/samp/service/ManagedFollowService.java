package com.kokochi.samp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.mapper.ManagedFollowMapper;

@Service
public class ManagedFollowService {
	
	@Autowired
	ManagedFollowMapper mapper;
	
	public boolean isManaged(ManagedFollow managed) throws Exception {
		ManagedFollow result = mapper.isManaged(managed);
		return result != null;
	}
	
	public void create(ManagedFollow managed) throws Exception {
		mapper.addManage(managed);
	}
	
	public void remove(ManagedFollow managed) throws Exception {
		mapper.deleteManage(managed);
	}
	
	public List<ManagedFollow> list(String user_id) throws Exception {
		return mapper.list(user_id);
	}
}
