package com.kokochi.samp.service;

import java.util.List;

import com.kokochi.samp.domain.TwitchKeyVO;

public interface TwitchKeyService {
	public void register(TwitchKeyVO key) throws Exception;
	
	public TwitchKeyVO read(String keyName) throws Exception;
	
	public void modify(TwitchKeyVO key) throws Exception;
	
	public void remove(String keyName) throws Exception;
	
	public List<TwitchKeyVO> list() throws Exception;
}
