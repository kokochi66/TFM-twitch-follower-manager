package com.kokochi.samp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kokochi.samp.domain.UserFollowVO;
import com.kokochi.samp.security.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.service.ManagedService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/test")
public class TestController {
	
	@Autowired
	private ManagedService follow_service;

	@Autowired
	private UserDetailService userService;

	@RequestMapping(value="/followtest", method=RequestMethod.GET)
	public String formTest(Model model) throws Exception {
		try {
			log.info("TestController - form GET");
			List<UserFollowVO> list = new ArrayList<>();
			for(int i=0;i<10;i++) {
				UserFollowVO userFollowVO = new UserFollowVO();
				userFollowVO.setId("Test"+i);
				userFollowVO.setFrom_id("101735242");
				userFollowVO.setTo_id("62356185");
				userFollowVO.setFollowed_at(new Date());
				list.add(userFollowVO);
			}
			System.out.println("TEST :: 리스트 추가하기");
			userService.addUserFollowList(list);
			System.out.println("TEST :: 리스트 추가성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "test/formTest";
	}
}
