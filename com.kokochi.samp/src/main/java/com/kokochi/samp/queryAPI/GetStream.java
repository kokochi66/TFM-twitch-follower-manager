package com.kokochi.samp.queryAPI;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;

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
				TwitchUser cUser = getUser(client_id, app_access_token, stream.getUser_id());
				
				stream.setProfile_image_url(cUser.getProfile_image_url());
				list.add(stream);
			}
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return list;
	}
	
	public TwitchUser getUser(String client_id, String app_access_token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<Stream> list = new ArrayList<>();
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users?id={id}", HttpMethod.GET,
					entity, String.class, user_id);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray jsonArray = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject cJson = (JSONObject) parser.parse(jsonArray.get(0).toString());
			
			TwitchUser twitchUser = new Gson().fromJson(cJson.toString(), TwitchUser.class);
			
			return twitchUser;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}

}
