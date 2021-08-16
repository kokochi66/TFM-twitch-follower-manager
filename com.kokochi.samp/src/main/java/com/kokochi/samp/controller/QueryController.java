package com.kokochi.samp.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.queryAPI.GetFollow;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.service.ManagedFollowService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/query")
@Slf4j
public class QueryController {
	
	@Autowired
	private TwitchKeyService key;
	
	@Autowired
	private ManagedFollowService follow_service;
	
	private GetVideo videoGetter = new GetVideo();
	private GetFollow followGetter = new GetFollow();
	
	@RequestMapping(value="/request/initFollow", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getReplayDataFromStream(@RequestHeader(value="user_id")String user_id, 
			@RequestHeader(value="twitch_user_id")String twitch_user_id) throws Exception {
		log.info("/query/request/initFollow - 팔로우 목록 초기화 및 동기화 ");
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		
		List<ManagedFollow> managedFollow_list = follow_service.list(user_id);
		
		HashSet<String> follow_set = new HashSet<>();
		List<String> follow_list = followGetter.getAllFollowedList(client_id, app_access_token, "from_id="+twitch_user_id);
		
		for(String str : follow_list) follow_set.add(str);
		
		for(ManagedFollow mf : managedFollow_list) {
			if(!follow_set.contains(mf.getTo_user())) {
				log.info("/query/request/initFollow - ManagedFollow 제거 :: " + mf.getTo_user());
				follow_service.remove(mf);
			}
		}
		return "success";
	}
}
