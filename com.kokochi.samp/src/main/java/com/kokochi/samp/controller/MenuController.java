package com.kokochi.samp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/menu")
@Slf4j
public class MenuController {
	
	@Autowired
	TwitchKeyService key;
	
	@RequestMapping(value="/setting", method = RequestMethod.GET)
	public String menuSetting(Model model) { // 메인 home 화면 매핑
		log.info("/menu/setting - 메뉴화면");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			model.addAttribute("setting_twich_user_id", user.getTwitch_user_id());
			model.addAttribute("setting_user_id", user.getUser_id());
			model.addAttribute("setting_user_email", user.getUser_email());
		}
		return "menu/setting";
	}
	
	@RequestMapping(value="/managefollow", method = RequestMethod.GET)
	public String menuFollow(Model model) throws Exception { // 메인 home 화면 매핑
		log.info("/menu/managefollow - ManagaFollow Mapping");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			// 로그인한 사용자의 팔로우 목록 가져오기
			
			UserDTO user = (UserDTO) principal;
			String client_id = key.read("client_id").getKey_value();
			String app_access_token = key.read("app_access_token").getKey_value();
			
			GetStream streamGenerator = new GetStream();
			List<TwitchUser> follow_list =  streamGenerator.getFollowedList(client_id, app_access_token, "login="+user.getTwitch_user_id()+"&");

			for(int i=0;i<follow_list.size();i++) {
				log.info(follow_list.get(i).toString());
			}
			
			model.addAttribute("follow_list", follow_list);
			
			return "menu/managefollow";
		} else {
			
			return "redirect:/";
			
		}
	}
	@RequestMapping(value="/replayvideo", method = RequestMethod.GET)
	public void postPOST() { // 메인 home 화면 매핑
		log.info("/menu/replayvideo - ReplayVideo Mappin");
	}
}
