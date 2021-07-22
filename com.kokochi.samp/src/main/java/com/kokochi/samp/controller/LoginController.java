package com.kokochi.samp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.Member;

@Controller
@RequestMapping(value="/auth")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(Locale locale, Model model) { // 메인 home 화면 매핑
		logger.info("/auth/login - Login Mapping :: Locale = "+ locale);
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String postGET(Locale locale, Model model) { // 메인 home 화면 매핑
		logger.info("/post - POST TEST");
		return "posttest";
	}
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	public String postPOST(Locale locale) { // 메인 home 화면 매핑
		return "GOOD";
	}
}
