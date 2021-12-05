package com.kokochi.samp.mapper;

import java.util.List;

import com.kokochi.samp.domain.TwitchKeyVO;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;

@Mapper
public interface TwitchKeyMapper {
	
	public void create(TwitchKeyVO key) throws Exception;
	
	public TwitchKeyVO read(String keyName) throws Exception;
	
	public void update(TwitchKeyVO key) throws Exception;
	
	public void delete(String keyName) throws Exception;
	
	public List<TwitchKeyVO> list() throws Exception;
}
