package com.kokochi.samp.queryAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.kokochi.samp.queryAPI.domain.Stream;

public class GetStream {
	
	private JSONParser parser = new JSONParser();
	
	public ArrayList<Stream> getLiveStreams(String client_id, String app_access_token, int first) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<Stream> list = new ArrayList<>();
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/streams?language={language}&first={first}", HttpMethod.GET,
					entity, String.class, "ko", first);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray jsonArray = (JSONArray) parser.parse(jsonfile.get("data").toString());
			
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(jsonArray.get(i).toString());
				
				Stream stream = new Gson().fromJson(cJson.toString(), Stream.class);
				list.add(stream);
			}
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return list;
	}

}
