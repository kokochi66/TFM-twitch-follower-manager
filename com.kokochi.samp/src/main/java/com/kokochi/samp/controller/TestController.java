package com.kokochi.samp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.service.ManagedService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/test")
public class TestController {
	
	@Autowired
	private ManagedService follow_service;
	
	@RequestMapping(value="/followtest", method=RequestMethod.GET)
	public String formTest(Model model) throws Exception {
		log.info("TestController - form GET");
		
		List<ManagedFollow> follow_list_limit = follow_service.list_numFollow("kokochi", 0, 8);
		
		return "test/formTest";
	}
	


}
