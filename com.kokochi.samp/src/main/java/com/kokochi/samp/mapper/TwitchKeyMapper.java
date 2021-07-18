package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.TwitchKey;

public interface TwitchKeyMapper {
	
	public void create(TwitchKey key) throws Exception;
	
	public TwitchKey read(String key_name) throws Exception;
	
	public void update(TwitchKey key) throws Exception;
	
	public void delete(String key_name) throws Exception;
	
	public List<TwitchKey> list() throws Exception;
}
