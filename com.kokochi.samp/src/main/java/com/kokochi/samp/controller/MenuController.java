package com.kokochi.samp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.queryAPI.GetFollow;
import com.kokochi.samp.queryAPI.GetStream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.queryAPI.innerProcess.PostQuery;
import com.kokochi.samp.service.ManagedFollowService;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/menu")
@Slf4j
public class MenuController {
	
	@Autowired
	private TwitchKeyService key;
	
	@Autowired
	private ManagedFollowService follow_service;
	
	private GetStream streamGenerator = new GetStream();
	private GetFollow followGetter = new GetFollow();
	private PostQuery postQuery = new PostQuery();
	
	@RequestMapping(value="/setting", method = RequestMethod.GET)
	public String menuSetting(Model model) { // 메인 home 화면 매핑
		log.info("/menu/setting - 메뉴화면");
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			
			model.addAttribute("setting_twich_user_login", user.getTwitch_user_login());
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
			postQuery.initManagedFollow(user.getTwitch_user_id(), user.getUser_id());
			String client_id = key.read("client_id").getKey_value();
			String app_access_token = key.read("app_access_token").getKey_value();
			
			List<TwitchUser> follow_list =  followGetter.getFollowedList(client_id, app_access_token, "from_id="+user.getTwitch_user_id()+"&", "first=40&");
			if(follow_list == null) return "redirect:/token/app_access_token";
			// 가져오기에 실패하면, 토큰을 재발급받아 다시 시도한다.
			
			for(int i=0;i<follow_list.size();i++) {
				follow_list.get(i).setChecking_managed(follow_service.isManaged(
						new ManagedFollow(user.getUser_id(), follow_list.get(i).getId())));
				if(follow_list.get(i).isChecking_managed()) {
					follow_list.add(0, follow_list.remove(i)); // 관리체크된 값들은 맨위로 올라오도록 리스트 위치를 조정해준다.
				}
			}
			
			model.addAttribute("follow_list", follow_list);
			
			return "menu/managefollow";
		} else {
			// 오류가 나면 메인화면으로 돌아간다.
			return "redirect:/";
		}
	}
	
	@RequestMapping(value="/replayvideo", method = RequestMethod.GET)
	public void postPOST() { // 메인 home 화면 매핑
		log.info("/menu/replayvideo - ReplayVideo Mappin");
	}
	
	
	@RequestMapping(value="/request/managedfollow/add", method = RequestMethod.POST)
	@ResponseBody
	public String addfollowed_request(@RequestHeader(value="user_id")String user_id, 
			@RequestHeader(value="to_user")String to_user) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 추가 " + user_id +" "+to_user);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			if(user.getUser_id().equals(user_id)) {
				follow_service.create(new ManagedFollow(user_id, to_user));
				return "success";
			}	// 현재 로그인된 값과 전달하는 아이디가 일치해야만 값을 적용하게 됨
		}
		
		return "failure";
	}
	
	@RequestMapping(value="/request/managedfollow/remove", method = RequestMethod.POST)
	@ResponseBody
	public String removefollowed_request(@RequestHeader(value="user_id")String user_id, @RequestHeader(value="to_user")String to_user) throws Exception {
		log.info("/managedfollow/add - 팔로우 관리목록 제거 " + user_id +" "+to_user);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			if(user.getUser_id().equals(user_id)) {
				follow_service.remove(new ManagedFollow(user_id, to_user));
				return "success";
			}
		}
		
		return "failure";
	}
}
