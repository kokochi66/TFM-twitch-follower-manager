package com.kokochi.samp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kokochi.samp.DTO.UserDTO;
import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.domain.ManagedVideoVO;
import com.kokochi.samp.service.ManagedService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value="/manage")
@Slf4j
public class ManageController {

	@Autowired
	private ManagedService managed_service;	
	
	@RequestMapping(value="/video/toggle", produces="application/json;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String toggleManagedVideoPost(@RequestBody String body) throws Exception {
		log.info("/manage/video/add POST - 나의 찜 다시보기 Toggle : " + body);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			ManagedVideoVO mVideo_body = new ManagedVideoVO("exex::", user.getUser_id(), body);
			if(managed_service.isManagedVideo(mVideo_body)) {
				managed_service.removeVideo(mVideo_body);
				return "remove " + body;
			}
			else {
				managed_service.createVideo(mVideo_body);
				return "add " + body;
			}
			
		}
		return "failed";
	}
	// /video/toggle POST - 다시보기를 관리목록에 추가/삭제
	
	@RequestMapping(value="/follow/toggle", method = RequestMethod.POST)
	@ResponseBody
	public String toggleManagedFollowPost(@RequestBody String body) throws Exception {
		log.info("/follow/toggle - 나의 스트리머 관리목록에 추가/삭제 : " + body);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!principal.toString().equals("anonymousUser")) {
			UserDTO user = (UserDTO) principal;
			ManagedFollowVO mFollow_body = new ManagedFollowVO("exex::", user.getUser_id(), body);
			if(managed_service.isManagedFollow(mFollow_body)) {
				managed_service.removeFollow(mFollow_body.toString());
				return "remove " + body;
			}
			else {
				managed_service.createFollow(mFollow_body);
				return "add " + body;
			}
			
		}
		return "failed";
	}
	// /follow/toggle POST - 스트리머를 관리목록에 추가/삭제


}
