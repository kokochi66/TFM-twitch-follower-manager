package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.mapper.UserMapper;
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
@Slf4j
@RequestMapping(value="/detail")
public class DetailController {
	
	@Autowired
	TwitchKeyService key;
	
	@Autowired
	UserMapper usermapper;
	
	@Autowired
	private ManagedFollowService follow_service;
	
	private JSONParser parser = new JSONParser();
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String detail(Model model) throws Exception { // 메인 home 화면 매핑
		log.info("/detail - 스트리머 상세보기 페이지");
		// 1. client_id를 가져오고, 인증토큰을 가져와서 라이브중인 스트림을 먼저 가져온다. 성공하면 그대로 뷰로 넘김
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetStream streamGenerator = new GetStream();
		List<Stream> headslide_list = streamGenerator.getLiveStreams(client_id, app_access_token, 5);
		
		// 2. 인증토큰의 기한이 끝난 경우에, 새로운 인증토큰을 생성하기 위해 client_secret을 가져오고, 인증토큰을 생성하여 성공시 그대로 뷰로 넘긴다.
		if(headslide_list == null) return "redirect:/token/app_access_token";
		
		// 성공시 모델에 리스트 입력 (입력전에, 썸네일 이미지의 크기를 조정해주어야 함.
		for(int i=0;i<headslide_list.size();i++) {
			String thumb_url = headslide_list.get(i).getThumbnail_url().replace("{width}", "400").replace("{height}", "250");
			headslide_list.get(i).setThumbnail_url(thumb_url);
		}

		model.addAttribute("headslide_list", headslide_list);
		
		
		GetVideo videoGetter = new GetVideo();
		GetStream streamGetter = new GetStream();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			List<ManagedFollow> follow_list = follow_service.list(user.getUser_id());
			// 팔로우 관리목록 가져오기
			
			List<Video> replay_video_list = videoGetter.getRecentVideoFromUsers(follow_list, client_id, user.getOauth_token(), 8);
			for(int i=0;i<replay_video_list.size();i++) {
//				log.info(replay_video_list.get(i).toString() + " " + replay_video_list.size());
				Video v = replay_video_list.get(i);
				TwitchUser tu = streamGetter.getUser(client_id, user.getOauth_token(), "id="+v.getUser_id());
				String thumb_url = replay_video_list.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200");
				replay_video_list.get(i).setThumbnail_url(thumb_url);
				replay_video_list.get(i).setProfile_url(tu.getProfile_image_url());
			}
			model.addAttribute("replay_video_list", replay_video_list);
			// 가져온 유저들의 가장 최근 영상들을 가져와 모델에 넣기
		}
		
		return "homes";
	}
	
	@RequestMapping(value="/home/request/getNextVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String removefollowed_request(@RequestHeader(value="service_map")String service_map, 
			@RequestHeader(value="service_target")String service_target) throws Exception {
//		log.info("/home/request/getNextVideo - 다음 비디오 가져오기 " + service_target +" " + service_map);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			Map<String, String> serviceMap = new HashMap<>();
			if(!service_map.equals("\"n\"") && service_map != null) {
				JSONArray service_arr = (JSONArray) parser.parse(service_map);
				for(int i=0;i<service_arr.size();i++) {
					JSONArray c_service = (JSONArray) parser.parse(service_arr.get(i).toString());
					serviceMap.put(c_service.get(0).toString(), c_service.get(1).toString());
				}
			} // n이 입력으로 들어오면 다음으로 넣어지는 값이 없음. 즉, Map의 값이 없게 세팅하여 가장 최근값을 리턴하도록 함.
			
			GetVideo videoGetter = new GetVideo();
			GetStream streamGetter = new GetStream();
			List<ManagedFollow> follow_list = follow_service.list(user.getUser_id());
			String client_id = key.read("client_id").getKey_value();
			
			List<Video> service_video = new ArrayList<>();
			
			if(service_target.equals("recent_video"))
				service_video = videoGetter.getRecentVideoFromUsersToNext(serviceMap, follow_list, client_id, user.getOauth_token(), 8);
			else if(service_target.equals("recent_live")) {
				int map_left = 0;
				if(serviceMap.size() > 0) {
					for(String key : serviceMap.keySet()) {
						map_left = Integer.parseInt(serviceMap.get(key));
						break;
					}
				}
//				System.out.println("getNextVideo - map_left :: " + map_left);
				List<ManagedFollow> follow_list_limit = follow_service.list_num(user.getUser_id(), map_left, map_left+8);
				
				for(int i=0;i<follow_list_limit.size();i++) {
//					System.out.println("getNextVideo - list add :: " + follow_list_limit.get(i).getTo_user());
					Stream s = streamGetter.getLiveStream(client_id, user.getOauth_token(), "user_id="+follow_list_limit.get(i).getTo_user());
					if(s != null) {
						s.setThumbnail_url(s.getThumbnail_url().replace("{width}", "300").replace("{height}", "200"));
						Video v = s.StreamToVideo();
						v.setNextPage(Integer.toString(map_left+8));
						service_video.add(v);
					}
//					System.out.println("getNextVideo - follow_list_limit :: " + s.StreamToVideo().toString());
				}
//				System.out.println("getNextVideo - service_video");
			}
			else if(service_target.equals("recent_clip")) {
				List<Clips> service_clip = videoGetter.getClipsRecentByUsers(serviceMap, follow_list, client_id, user.getOauth_token(), 8);
				for(int i=0;i<service_clip.size();i++) {
//					System.out.println("getNextVideo - recent_clip :: " + service_clip.get(i).toString());
					service_video.add(service_clip.get(i).clipsToVideo());
				}
			}
			
			
			JSONArray res_arr = new JSONArray();
			for(int i=0;i<service_video.size();i++) {
//				log.info("service_video :: " + service_video.get(i).toString());
				service_video.get(i).setThumbnail_url(service_video.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
				TwitchUser tu = streamGetter.getUser(client_id, user.getOauth_token(), "id="+service_video.get(i).getUser_id());
				service_video.get(i).setProfile_url(tu.getProfile_image_url());
				
				JSONObject res_ob = service_video.get(i).parseToJSONObject();
				res_arr.add(res_ob);
			}
			if(res_arr.size() <= 0) return null;
//			log.info(res_arr.toJSONString());
			return res_arr.toJSONString();
		}
		
		return "failure";
	}
}
