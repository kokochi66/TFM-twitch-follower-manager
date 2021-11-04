package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.domain.ManagedVideoVO;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetClips;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Clips;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.Video;
import com.kokochi.samp.service.ManagedService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	private TwitchKeyService key;
	
	@Autowired
	private UserMapper usermapper;
	
	@Autowired
	private ManagedService managed_service;
	
	private GetStream streamGetter = new GetStream();
	private GetVideo videoGetter = new GetVideo();
	private GetClips clipGetter = new GetClips();
	
	@RequestMapping(value="/")
	public String home(Model model) throws Exception { // 
		log.info("/ - 메인경로 이동");
		return "homes";
	}
	// /home 매핑
	
	@RequestMapping(value="/home/request/getLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getLiveVideo() throws Exception { 
		log.info("/home/request/getLiveVideo - 라이브 비디오 가져오기 :: ");
		
		String client_id = key.read("client_id").getKeyValue();
		String app_access_token = key.read("app_access_token").getKeyValue();
		List<Stream> headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
		if(headslide_list == null) headslide_list = streamGetter.getLiveStreams(client_id, app_access_token, 5);
		// 토큰 오류시에는 토큰 초기화 후에 재접근, 재접근해도 값을 가져올 수 없으면 오류임
		
//		log.info("headslide_list :: 가져옴 - " + headslide_list.size());
		
		JSONArray res_arr = new JSONArray();
		for(int i=0;i<headslide_list.size();i++) {
//			log.info("service_video :: " + service_video.get(i).toString());
			headslide_list.get(i).setThumbnail_url(headslide_list.get(i).getThumbnail_url().replace("{width}", "400").replace("{height}", "250"));
			
			JSONObject res_ob = headslide_list.get(i).StreamToJSON();
			res_arr.add(res_ob);
		}
		if(res_arr.size() <= 0) return null;
//		log.info(res_arr.toJSONString());
		return res_arr.toJSONString();
	}
	// 라이브 비디오 가져오기
	
	@RequestMapping(value="/home/request/getMyRecentVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyRecentVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyRecentVideo - 나의 관리목록 최신 다시보기 가져오기 " + body);
		JSONParser parser = new JSONParser();
		JSONArray res_arr = new JSONArray();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getUser_id());
			String client_id = key.read("client_id").getKeyValue();
			List<Video> service_video = new ArrayList<>();
//			log.info("getMyRecentVideo");
			service_video = videoGetter.getRecentVideoFromUsers(client_id, user.getOauth_token(), follow_list, "first=8");
//			log.info("getMyRecentVideo :: " + service_video.size());
			
			for(int i=0;i<service_video.size();i++) {
				service_video.get(i).setThumbnail_url(service_video.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
				service_video.get(i).setManaged(managed_service.isManagedVideo(new ManagedVideoVO("exex::", user.getUser_id(),
						service_video.get(i).getId())));
				JSONObject res_ob = service_video.get(i).parseToJSONObject();
//				log.info("getMyRecentVideo :: " + res_ob.toJSONString());
				res_arr.add(res_ob);
			}
			if(res_arr.size() <= 0) return null;
		}
//		log.info(res_arr.toJSONString());
		return res_arr.toJSONString();
	}
	// 관리목록 최신 다시보기 
	
	@RequestMapping(value="/home/request/getMyRecentVideo/next", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyRecentVideoNext(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyRecentVideo/next - 나의 관리목록 최신 다시보기 더보기 가져오기 " + body);
		try {
			JSONParser parser = new JSONParser();
			JSONArray res_arr = new JSONArray();
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(!principal.toString().equals("anonymousUser")) {
				UserDTO user = (UserDTO) principal;
				
				JSONArray service_arr = (JSONArray) parser.parse(body);
				List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getUser_id());
				String client_id = key.read("client_id").getKeyValue();
				Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
				List<Video> service_video = new ArrayList<>();
				service_video = videoGetter.getRecentVideoFromUserNext(client_id, user.getOauth_token(), 
						service_arr.get(0).toString(), "first=8&after="+service_arr.get(1).toString());
				
				for(int i=0;i<service_video.size();i++) {
					service_video.get(i).setThumbnail_url(service_video.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
					service_video.get(i).setManaged(managed_service.isManagedVideo(new ManagedVideoVO("exex::", user.getUser_id(), service_video.get(i).getId())));
					JSONObject res_ob = service_video.get(i).parseToJSONObject();
					res_arr.add(res_ob);
				}
				if(res_arr.size() <= 0) return null;
			}
//			log.info(res_arr.toJSONString());
			return res_arr.toJSONString();
			
		} catch(Exception e) {
			return e.toString();
		}
	}
	// 관리목록 최신 다시보기 더보기
	
	@RequestMapping(value="/home/request/getMyLiveVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyLiveVideo(@RequestBody String body) throws Exception {
//		log.info("/home/request/getMyLiveVideo - 나의 관리목록 라이브 가져오기 " + body);
		JSONArray res_arr = new JSONArray();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getUser_id());
			String client_id = key.read("client_id").getKeyValue();
			
			for(int i=0;i<follow_list.size();i++) {
				Stream s = streamGetter.getLiveStream(client_id, user.getOauth_token(), follow_list.get(i).getToUser(), "");
				if(s != null) {
//					log.info("service_getLive :: " + s.toString() +" " + i +" " + follow_list.size())s;
					s.setThumbnail_url(s.getThumbnail_url().replace("{width}", "300").replace("{height}", "200"));
					JSONObject res_ob = s.StreamToVideo().parseToJSONObject();
					res_arr.add(res_ob);
				}
			}
//			log.info("video format :: " + res_arr.toJSONString());
			if(res_arr.size() <= 0) return null;
		}
		
		return res_arr.toJSONString();
	}
	// 관리목록 라이브
	
	@RequestMapping(value="/home/request/getMyClipVideo", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyClipVideo(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyClipVideo - 나의 관리목록 클립 가져오기 " + body);
		JSONArray res_arr = new JSONArray();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getUser_id());
			String client_id = key.read("client_id").getKeyValue();
			List<Clips> service_clip = clipGetter.getClipsRecentByUsers(follow_list, client_id, user.getOauth_token(), "first=8");
			
			for(int i=0;i<service_clip.size();i++) {
				service_clip.get(i).setThumbnail_url(service_clip.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
				JSONObject res_ob = service_clip.get(i).clipsToVideo().parseToJSONObject();
				res_arr.add(res_ob);
			}
			if(res_arr.size() <= 0) return null;
//			log.info(res_arr.toJSONString());
		}
		return res_arr.toJSONString();
	}
	// 관리목록 인기클립
	
	@RequestMapping(value="/home/request/getMyClipVideo/next", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String getMyClipVideoNext(@RequestBody String body) throws Exception {
		log.info("/home/request/getMyClipVideo/next - 나의 관리목록 클립 가져오기 Next " + body);
		JSONParser parser = new JSONParser();
		JSONArray res_arr = new JSONArray();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;

			JSONArray service_arr = (JSONArray) parser.parse(body);
			List<ManagedFollowVO> follow_list = managed_service.listFollow(user.getUser_id());
			String client_id = key.read("client_id").getKeyValue();
			List<Clips> service_clip = clipGetter.getClipsRecentByUser(client_id, user.getOauth_token(), service_arr.get(0).toString()
					, "after="+service_arr.get(1).toString()+"&first=8");
			for(int i=0;i<service_clip.size();i++) {
				service_clip.get(i).setThumbnail_url(service_clip.get(i).getThumbnail_url().replace("%{width}", "300").replace("%{height}", "200"));
				JSONObject res_ob = service_clip.get(i).clipsToVideo().parseToJSONObject();
				res_arr.add(res_ob);
			}
			if(res_arr.size() <= 0) return null;
		}
//		log.info(res_arr.toJSONString());
		return res_arr.toJSONString();
	}
	// 관리목록 인기클립 더보기
}
