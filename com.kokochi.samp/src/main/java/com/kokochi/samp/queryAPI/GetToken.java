package com.kokochi.samp.queryAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.service.TwitchKeyService;

public class GetToken {
	
	public JSONObject GetAppAccessToken(String client_id, String client_secret) throws Exception {
		
		JSONParser parser = new JSONParser();
		MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();
		HttpHeaders headers = new HttpHeaders();
		params.add("client_id", client_id);
		params.add("client_secret", client_secret);
		params.add("grant_type", "client_credentials");
		
		HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
				"https://id.twitch.tv/oauth2/token", HttpMethod.POST,
				entity, String.class);
		
		JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
		
		return jsonfile;
	}
	
	public JSONObject GetOauth2AuthorizeToken(String client_id, String client_secret, String code) throws Exception {
		
		MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();
		JSONParser parser = new JSONParser();
		HttpHeaders headers = new HttpHeaders();
		params.add("client_id", client_id);
		params.add("client_secret", client_secret);
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "http://localhost:8080/auth/login/oauth2/code/twitch");
		
		
		HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
				"https://id.twitch.tv/oauth2/token", HttpMethod.POST, entity, String.class);
		
		JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
		
		return jsonfile;
	}
	
	public void requestAppAccessToken() {
		System.out.println("tokenGetter - requestAppAccessToken");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		try {
			ResponseEntity<String> response = rt.exchange(
					"/token//app_access_token", HttpMethod.POST, entity, String.class);
		} catch (HttpStatusCodeException  e) {}
	}

}
