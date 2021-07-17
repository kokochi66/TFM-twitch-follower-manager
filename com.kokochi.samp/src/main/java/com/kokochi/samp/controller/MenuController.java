package com.kokochi.samp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.User;

@Controller
@RequestMapping(value="/menu")
public class MenuController {
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@RequestMapping(value="/setting", method = RequestMethod.GET)
	public void home(Locale locale, Model model) { // 메인 home 화면 매핑
		logger.info("/menu/setting - Setting Mapping :: Locale = "+ locale);
	}
	
	@RequestMapping(value="/managefollow", method = RequestMethod.GET)
	public void postGET(Locale locale, Model model) { // 메인 home 화면 매핑
		logger.info("/menu/managefollow - ManagaFollow Mapping :: Locale = "+ locale);
	}
	@RequestMapping(value="/replayvideo", method = RequestMethod.GET)
	public void postPOST(Locale locale, User user) { // 메인 home 화면 매핑
		logger.info("/menu/replayvideo - ReplayVideo Mapping :: Locale = "+ locale);
	}
}
