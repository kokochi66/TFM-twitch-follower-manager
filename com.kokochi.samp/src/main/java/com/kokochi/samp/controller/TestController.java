package com.kokochi.samp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kokochi.samp.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/test")
public class TestController {
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String formTest(Model model) {
		log.info("TestController - form GET");
		return "test/formTest";
	}
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String formPost(Model model, Member member) {
		log.info("TestController - form POST" + member.toString());
		return "redirect:/";
	}
}
