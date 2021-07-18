package com.kokochi.samp.service;

import java.util.List;

import com.kokochi.samp.domain.TwitchKey;

public interface TwitchKeyService {
	public void register(TwitchKey key) throws Exception;
	
	public TwitchKey read(String key_name) throws Exception;
	
	public void modify(TwitchKey key) throws Exception;
	
	public void remove(String key_name) throws Exception;
	
	public List<TwitchKey> list() throws Exception;
}
