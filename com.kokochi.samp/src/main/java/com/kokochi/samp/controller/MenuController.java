package com.kokochi.samp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.Member;

@Controller
@RequestMapping(value="/menu")
public class MenuController {
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@RequestMapping(value="/setting", method = RequestMethod.GET)
	public void home(Model model) { // 메인 home 화면 매핑
		logger.info("/menu/setting - 메뉴화면");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal.toString().equals("anonymousUser")) {

		} else {
			UserDTO user = (UserDTO) principal;
			model.addAttribute("setting_twich_user_id", user.getTwitch_user_id());
			model.addAttribute("setting_user_id", user.getUser_id());
			model.addAttribute("setting_user_email", user.getUser_email());
		}
	}
	
	@RequestMapping(value="/managefollow", method = RequestMethod.GET)
	public void postGET(Model model) { // 메인 home 화면 매핑
		logger.info("/menu/managefollow - ManagaFollow Mapping :: Locale = ");
	}
	@RequestMapping(value="/replayvideo", method = RequestMethod.GET)
	public void postPOST() { // 메인 home 화면 매핑
		logger.info("/menu/replayvideo - ReplayVideo Mapping :: Locale = ");
	}
}
