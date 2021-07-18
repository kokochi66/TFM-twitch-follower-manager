package com.kokochi.samp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kokochi.samp.domain.TwitchKey;
import com.kokochi.samp.mapper.TwitchKeyMapper;

@Service
public class TwitchKeyServiceImpl implements TwitchKeyService {
	
	@Autowired
	TwitchKeyMapper mapper;

	@Override
	public void register(TwitchKey key) throws Exception {
		// TODO Auto-generated method stub
		mapper.create(key);
	}

	@Override
	public TwitchKey read(String key_name) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("key 가져오기 = " + key_name );
		
		TwitchKey key = mapper.read(key_name);
		
		if(key == null) {
			System.out.println("key가 널값임");
			
			key = new TwitchKey();
			key.setKey_name(key_name);
			
			key.setKey_value("");	// 해당하는 키의 값을 받아올 수 있도록 하는 로직이 필요함.
			
			register(key);
		}
		
		System.out.println(key);
		
		return key;
	}

	@Override
	public void modify(TwitchKey key) throws Exception {
		// TODO Auto-generated method stub
		mapper.update(key);
	}

	@Override
	public void remove(String key_name) throws Exception {
		// TODO Auto-generated method stub
		mapper.delete(key_name);
	}

	@Override
	public List<TwitchKey> list() throws Exception {
		// TODO Auto-generated method stub
		return mapper.list();
	}

}
