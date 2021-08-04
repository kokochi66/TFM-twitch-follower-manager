package com.kokochi.samp.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String postTest(Model model) {
		log.info("TestController - testpost GET");
		
		model.addAttribute("user_id", "OAuth2_authentication");
		model.addAttribute("user_pwd", "Bearer tyhale4papbvxneiwto906bzicbopt");
		
		return "auth/hiddenLogin";
	}

}
