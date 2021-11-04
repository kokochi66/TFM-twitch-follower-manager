package com.kokochi.samp.service;

import java.util.List;

import com.kokochi.samp.domain.TwitchKeyVO;

public interface TwitchKeyService {
	public void register(TwitchKeyVO key) throws Exception;
	
	public TwitchKeyVO read(String key_name) throws Exception;
	
	public void modify(TwitchKeyVO key) throws Exception;
	
	public void remove(String key_name) throws Exception;
	
	public List<TwitchKeyVO> list() throws Exception;
}
