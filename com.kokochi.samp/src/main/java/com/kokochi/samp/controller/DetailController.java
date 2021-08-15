package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Clips;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.queryAPI.domain.Video;
import com.kokochi.samp.service.ManagedFollowService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/detail")
@Slf4j
public class DetailController {
	
	@Autowired
	TwitchKeyService key;
	
	@Autowired
	UserMapper usermapper;
	
	@Autowired
	private ManagedFollowService follow_service;
	
	private JSONParser parser = new JSONParser();
	
	@RequestMapping(method=RequestMethod.GET)
	public String detail(Model model, @RequestParam("streams")String streams) throws Exception { // 메인 home 화면 매핑
		log.info("/detail - 스트리머 상세보기 페이지 :: " + streams);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		
		GetStream streamGetter = new GetStream();
		TwitchUser stream = streamGetter.getUser(client_id, app_access_token, "id="+streams);
		if(stream == null) return"detail/findError";
		
		model.addAttribute("stream", stream);
		return "detail/detail";
	}
	
	@RequestMapping(value="/request/live", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getLiveDataFromStream(@RequestHeader(value="login")String login) throws Exception {
		log.info("/detail/request/live - 라이브 데이터 가져오기 :: " + login);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetStream streamGetter = new GetStream();
		Stream stream = streamGetter.getLiveStream(client_id, app_access_token, "user_id="+login);
		if(stream == null) {
			JSONObject res = new JSONObject();
			res.put("title", "방송중이 아님");
			res.put("user_login", "");
			res.put("thumbnail_url", "/resources/assets/img/default_image.jpg");
			return res.toJSONString();
		}
		stream.setThumbnail_url(stream.getThumbnail_url().replace("{width}", "1920").replace("{height}", "1080"));
		
		JSONObject res = stream.StreamToJSON();
		return res.toJSONString();
	}
	
	@RequestMapping(value="/request/replay", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getReplayDataFromStream(@RequestHeader(value="login")String login,
			@RequestHeader(value="next")String next) throws Exception {
		log.info("/detail/request/replay - 다시보기 데이터 가져오기 :: " + login);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetVideo videoGetter = new GetVideo();
		ArrayList<Video> replay_list = videoGetter.getVideoFromId(client_id, app_access_token, "user_id="+login+"&"+next, 8);
		if(replay_list == null || replay_list.size() == 0) return "error";
		
		JSONArray res_arr = new JSONArray();
		for(int i=0;i<replay_list.size();i++) {
			replay_list.get(i).setThumbnail_url(replay_list.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
			JSONObject j = replay_list.get(i).parseToJSONObject();
			res_arr.add(j);
		}
		return res_arr.toJSONString();
	}
	
	@RequestMapping(value="/request/clips", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getClipsDataFromStream(@RequestHeader(value="login")String login,
			@RequestHeader(value="next")String next) throws Exception {
		log.info("/detail/request/clips - 클립 데이터 가져오기 :: " + login);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetClips clipGetter = new GetClips();
		List<Clips> replay_list = clipGetter.getClipsByUserId(client_id, app_access_token, "broadcaster_id="+login+"&"+next, 8);
		if(replay_list == null || replay_list.size() == 0) return "error";
		
		JSONArray res_arr = new JSONArray();
		for(int i=0;i<replay_list.size();i++) {
			JSONObject j = replay_list.get(i).clipsToJSON();
			res_arr.add(j);
		}
		return res_arr.toJSONString();
	}
	
	@RequestMapping(value="/request/relative", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getRelativeDataFromStream(@RequestHeader(value="login")String login) throws Exception {
		log.info("/detail/request/relative - 연관 스트리머 데이터 가져오기 :: " + login);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetStream streamGetter = new GetStream();
		
		Map<String, Integer> relative = new HashMap<>();
		streamGetter.getRelativeFollow(relative, client_id, app_access_token, "to_id="+login+"&", "", 0);
		if(relative == null || relative.size() == 0) return "error";
		
		List<Entry<String, Integer>> SortEntry = new ArrayList<>();
		for(Entry<String, Integer> entry : relative.entrySet()) {
			if(entry.getValue() >= 10) SortEntry.add(entry);
		}
		Collections.sort(SortEntry, (a,b) -> b.getValue().compareTo(a.getValue()));
//		for(int i=SortEntry.size()-1;i>=0;i--) {
//			log.info(SortEntry.get(i).getKey() +" " + SortEntry.get(i).getValue());
//		}
		JSONArray res_arr = new JSONArray();
		for(int i=1;i<SortEntry.size();i++) {
			res_arr.add(SortEntry.get(i).getKey());
		}
		return res_arr.toJSONString();
	}
	
	@RequestMapping(value="/request/getTwitchUserSet", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getTwitchUserDataFromStream(@RequestHeader(value="login_arr")String login_arr) throws Exception {
		log.info("/detail/request/getTwitchUserSet - 트위치 사용자 데이터 가져오기");
		JSONArray res_arr = new JSONArray();
		JSONArray service_arr = (JSONArray) parser.parse(login_arr);
		
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetStream streamGetter = new GetStream();
		
		for(int i=0;i<service_arr.size();i++) {
			String c_user_id = service_arr.get(i).toString();
			TwitchUser t_us = streamGetter.getUser(client_id, app_access_token, "id="+c_user_id+"&");
			JSONObject j = t_us.TwitchUserToJSON();
			res_arr.add(j);
		}

		return res_arr.toJSONString();
	}
}
