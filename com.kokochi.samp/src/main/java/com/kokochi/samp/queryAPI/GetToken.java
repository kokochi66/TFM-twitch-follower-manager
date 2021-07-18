package com.kokochi.samp.queryAPI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kokochi.samp.service.TwitchKeyService;

public class GetToken {
	
	private JSONParser parser = new JSONParser();
	@Autowired
	private TwitchKeyService key;
	
	public JSONObject GetAppAccessToken() throws Exception {
		
		String client_id = key.read("client_id").getKey_value();
		String client_secret = key.read("client_secret").getKey_value();
		
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
		System.out.println(jsonfile.get("access_token"));
		
		return jsonfile;
	}

}
