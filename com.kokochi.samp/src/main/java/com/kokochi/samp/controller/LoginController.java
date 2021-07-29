package com.kokochi.samp.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.security.UserDetailService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/auth")
@Slf4j
public class LoginController {
	
	@Autowired
	UserDetailService detail;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public void login(Locale locale, Model model) { // 메인 home 화면 매핑
		log.info("/auth/login - Login Mapping :: Locale = "+ locale);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public void loginPage(Model model) {
		log.info("login POST Mapping");
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public void register(Model model) {
		log.info("/auth/regitser GET - 회원가입 페이지로 이동");
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerPro(Model model, Member member) throws Exception {
		log.info("/auth/register POST - 회원가입 처리 => " + member.toString());
		detail.userRegister(member);
		return "redirect:/auth/login";
	}
	

}
