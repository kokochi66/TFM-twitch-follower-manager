package com.kokochi.samp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.service.ManagedFollowService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/test")
public class TestController {
	
	@Autowired
	private ManagedFollowService follow_service;
	
	@RequestMapping(value="/managed", method=RequestMethod.GET)
	public String formTest(Model model) throws Exception {
		log.info("TestController - form GET");
		
		log.info(follow_service.isManaged(new ManagedFollow("kokochi", "asdasdasd"))+" ");
		
		return "test/formTest";
	}
	


}
