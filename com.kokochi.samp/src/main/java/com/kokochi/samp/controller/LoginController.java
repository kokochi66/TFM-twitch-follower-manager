package com.kokochi.samp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/auth")
@Slf4j
public class LoginController {
	
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(Locale locale, Model model) { // 메인 home 화면 매핑
		log.info("/auth/login - Login Mapping :: Locale = "+ locale);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void loginPage(Model model) {
		log.info("login POST Mapping");
	}

}
