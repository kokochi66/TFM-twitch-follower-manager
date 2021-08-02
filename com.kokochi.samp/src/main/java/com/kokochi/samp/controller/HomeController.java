package com.kokochi.samp.controller;

import java.util.List;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.TwitchKey;
import com.kokochi.samp.mapper.UserMapper;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.GetToken;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	TwitchKeyService key;
	
	@Autowired
	UserMapper usermapper;
	
	@RequestMapping(value="/")
	public String home(Locale locale, Model model) throws Exception { // 메인 home 화면 매핑
		log.info("/ - 메인경로 이동");
		// 1. client_id를 가져오고, 인증토큰을 가져와서 라이브중인 스트림을 먼저 가져온다. 성공하면 그대로 뷰로 넘김
		String client_id = key.read("client_id").getKey_value();
		String app_access_token = key.read("app_access_token").getKey_value();
		GetStream streamGenerator = new GetStream();
		List<Stream> headslide_list = streamGenerator.getLiveStreams(client_id, app_access_token, 5);
		
		// 2. 인증토큰의 기한이 끝난 경우에, 새로운 인증토큰을 생성하기 위해 client_secret을 가져오고, 인증토큰을 생성하여 성공시 그대로 뷰로 넘긴다.
		if(headslide_list == null) {
			GetToken tokenGenerator = new GetToken();
			String client_secret = key.read("client_secret").getKey_value();
			
			TwitchKey twitchkey = new TwitchKey();
			twitchkey.setKey_name("app_access_token");
			JSONObject getToken = tokenGenerator.GetAppAccessToken(client_id, client_secret);
			app_access_token = "Bearer"+" "+getToken.get("access_token");
			twitchkey.setKey_value(app_access_token);
			key.modify(twitchkey);
			headslide_list = streamGenerator.getLiveStreams(client_id, app_access_token, 5);
		}
		
		// 3. 실패시 에러페이지를 띄운다. -> 추후에 추가.
		
//		headslide_list.get(0).getThumbnail_url().replace("{width}", "400");
//		headslide_list.get(0).getThumbnail_url().replace("{height}", "250");
		
		
		// 성공시 모델에 리스트 입력 (입력전에, 썸네일 이미지의 크기를 조정해주어야 함.
		for(int i=0;i<headslide_list.size();i++) {
			String thumb_url = headslide_list.get(i).getThumbnail_url().replace("{width}", "400").replace("{height}", "250");
			headslide_list.get(i).setThumbnail_url(thumb_url);
		}

		model.addAttribute("headslide_list", headslide_list);
		
		return "homes";
	}
}
