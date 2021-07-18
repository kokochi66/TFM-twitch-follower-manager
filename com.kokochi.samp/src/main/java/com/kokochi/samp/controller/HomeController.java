package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.User;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.service.TwitchKeyService;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	TwitchKeyService key;
	
	@RequestMapping(value="/")
	public String home(Locale locale, Model model) throws Exception { // 메인 home 화면 매핑
		logger.info("/ - Home Mapping :: Locale = "+ locale);
		// 1. client_id를 가져오고, 인증토큰을 가져와서 라이브중인 스트림을 먼저 가져온다. 성공하면 그대로 뷰로 넘김
		// 2. 
		
		key.read("client_id").getKey_value();
		
//		GetStream streamGenerator = new GetStream();
//		ArrayList<Stream> list = streamGenerator.getLiveStreams();
		
		return "homes";
	}
	
	@RequestMapping(value="/post", method = RequestMethod.GET)
	public String postGET(Locale locale, Model model) { // 메인 home 화면 매핑
		logger.info("/post - POST TEST");
		return "posttest";
	}
	@RequestMapping(value="/post", method = RequestMethod.POST)
	public String postPOST(Locale locale, User user) { // 메인 home 화면 매핑
		logger.info("/post - POST TEST MEMBER = " + user.toString());
		return "GOOD";
	}
}
