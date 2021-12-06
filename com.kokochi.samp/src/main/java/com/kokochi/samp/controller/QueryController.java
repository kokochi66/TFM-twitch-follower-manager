package com.kokochi.samp.controller;

import java.util.HashSet;
import java.util.List;

import com.kokochi.samp.domain.UserFollowVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.queryAPI.GetFollow;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.service.ManagedService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/query")
@Slf4j
public class QueryController {
	
	@Autowired
	private TwitchKeyService key;
	
	@Autowired
	private ManagedService follow_service;
	
	private GetVideo videoGetter = new GetVideo();
	private GetFollow followGetter = new GetFollow();
	
	@RequestMapping(value="/request/initFollow", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getReplayDataFromStream(@RequestHeader(value="user_id")String user_id, 
			@RequestHeader(value="twitch_user_id")String twitch_user_id) throws Exception {
		log.info("/query/request/initFollow - 팔로우 목록 초기화 및 동기화 ");
		String client_id = key.read("client_id").getKeyValue();
		String app_access_token = key.read("app_access_token").getKeyValue();
		
		List<ManagedFollowVO> managedFollow_VO_list = follow_service.listFollow(user_id);

		log.info("/query/request/initFollow - 토큰값 :: ["+client_id+"]  ["+app_access_token+"]");
		HashSet<String> follow_set = new HashSet<>();
		List<UserFollowVO> follow_list = followGetter.getAllFollowedList(client_id, app_access_token, "from_id="+twitch_user_id);
		
		for(UserFollowVO follow : follow_list) follow_set.add(follow.getId());
		
		for(ManagedFollowVO mf : managedFollow_VO_list) {
			if(!follow_set.contains(mf.getTo_user())) {
				log.info("/query/request/initFollow - ManagedFollow 제거 :: " + mf.getTo_user());
				follow_service.removeFollow(mf.getUser_id());
			}
		}
		return "success";
	}
	
	@RequestMapping(value="/request/searchStreams", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public String getSearchStream(@RequestBody String query) throws Exception {
		log.info("/query/request/searchStreams - 검색창 쿼리 :: " + query);
		String client_id = key.read("client_id").getKeyValue();
		String app_access_token = key.read("app_access_token").getKeyValue();
//		log.info("/query/request/initFollow - 토큰값 :: ["+client_id+"]  ["+app_access_token+"]");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);	// 인증값을 요청 헤더에 넣어줌
		HttpEntity entity = new HttpEntity(headers); // 헤더 값을 entity로 넣어줌
		RestTemplate rt = new RestTemplate();
		JSONParser parser = new JSONParser();
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/search/channels?first=5&query="+query, HttpMethod.GET,
					entity, String.class);	// 트위치 API에 query값을 담아서 요청을 보냄
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
//			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
//			System.out.println("GetVideo - GetClipsOneByUserId :: " + clip.toString());
			log.info("success");
			return data.toJSONString();	// 결과값을 타입변환하여 반환해줌
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			log.info("getSearchStream " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return "failed";
		}	// 에러처리
		
		return "failed";	// 정상적이지 않은 경우에는 failed를 반환함.
	}
}
