package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetVideo;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.queryAPI.domain.Video;
import com.kokochi.samp.service.ManagedFollowService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	TwitchKeyService key;
	
	@Autowired
	UserMapper usermapper;
	
	@Autowired
	private ManagedFollowService follow_service;
	
	@RequestMapping(value="/")
	public String home(Locale locale, Model model) throws Exception { // 메인 home 화면 매핑
		log.info("/ - 메인경로 이동");
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
			
			// 나의 팔로우 관리 목록의 가장 최근 다시보기 영상 가져오기 쿼리
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
}
