package com.kokochi.samp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.service.ManagedFollowService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/request")
@Slf4j
public class RequestController {
	
	@Autowired
	private ManagedFollowService follow_service;
	
	@RequestMapping(value="/managedfollow/add", method = RequestMethod.POST)
	@ResponseBody
	public String addfollowed_request(@RequestHeader(value="user_id")String user_id, @RequestHeader(value="to_user")String to_user) throws Exception {
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
	
	@RequestMapping(value="/managedfollow/remove", method = RequestMethod.POST)
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
