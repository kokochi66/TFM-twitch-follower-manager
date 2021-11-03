package com.kokochi.samp.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kokochi.samp.domain.TwitchKey;
import com.kokochi.samp.queryAPI.GetToken;
import com.kokochi.samp.service.TwitchKeyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/token")
public class TokenController {
	
	@Autowired
	private TwitchKeyService key;
	
	private GetToken tokenGenerator = new GetToken();
	
	@RequestMapping(value="/app_access_token", method=RequestMethod.POST)
	@ResponseBody
	public String create_app_access_token() throws Exception {
		log.info("app_access_token generated");
		
		String client_id = key.read("client_id").getKeyValue();
		String client_secret = key.read("client_secret").getKeyValue();
		
		TwitchKey twitchkey = new TwitchKey();
		twitchkey.setKeyName("app_access_token");
		JSONObject getToken = tokenGenerator.GetAppAccessToken(client_id, client_secret);
		String app_access_token = "Bearer"+" "+getToken.get("access_token");
		twitchkey.setKeyValue(app_access_token);
		key.modify(twitchkey);
		return "Success";
	}
}
