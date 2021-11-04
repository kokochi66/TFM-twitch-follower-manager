package com.kokochi.samp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokochi.samp.domain.TwitchKeyVO;
import com.kokochi.samp.mapper.TwitchKeyMapper;

@Service
public class TwitchKeyServiceImpl implements TwitchKeyService {
	
	@Autowired
	TwitchKeyMapper mapper;

	@Override
	public void register(TwitchKeyVO key) throws Exception {
		// TODO Auto-generated method stub
		mapper.create(key);
	}

	@Override
	public TwitchKeyVO read(String key_name) throws Exception {
		// TODO Auto-generated method stub
		
		TwitchKeyVO key = mapper.read(key_name);
		
		if(key == null) {
			key = new TwitchKeyVO();
			key.setKeyName(key_name);
			key.setKeyValue("");	// 해당하는 키의 값을 받아올 수 있도록 하는 로직이 필요함.
			register(key);
		}
		return key;
	}

	@Override
	public void modify(TwitchKeyVO key) throws Exception {
		// TODO Auto-generated method stub
		mapper.update(key);
	}

	@Override
	public void remove(String key_name) throws Exception {
		// TODO Auto-generated method stub
		mapper.delete(key_name);
	}

	@Override
	public List<TwitchKeyVO> list() throws Exception {
		// TODO Auto-generated method stub
		return mapper.list();
	}

}
