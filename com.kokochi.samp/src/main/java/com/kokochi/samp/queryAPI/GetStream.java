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
import com.google.gson.GsonBuilder;
import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.queryAPI.domain.TwitchUser;

public class GetStream {
	
	private JSONParser parser = new JSONParser();
	private Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
	
	public Stream getLiveStream(String client_id, String app_access_token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/streams?"+user_id, HttpMethod.GET,
					entity, String.class, "ko");
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject cJson = (JSONObject) parser.parse(data.get(0).toString());
			Stream stream = gsonParser.fromJson(cJson.toString(), Stream.class);
			
			TwitchUser cUser = getUser(client_id, app_access_token, "id="+stream.getUser_id()+"&");
			stream.setProfile_image_url(cUser.getProfile_image_url());

			return stream;
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
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
				
				Stream stream = gsonParser.fromJson(cJson.toString(), Stream.class);
				TwitchUser cUser = getUser(client_id, app_access_token, "id="+stream.getUser_id()+"&");
				
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
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users?"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			JSONObject cJson = (JSONObject) parser.parse(data.get(0).toString());
			
			TwitchUser twitchUser = gsonParser.fromJson(cJson.toString(), TwitchUser.class);
			
			return twitchUser;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
	public ArrayList<TwitchUser> getFollowedList(String client_id, String app_access_token, String user_id, int first) throws Exception {
		
		TwitchUser user = getUser(client_id, app_access_token, user_id);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<TwitchUser> list = new ArrayList<>();
		try {
			String from_id = "from_id="+user.getId()+"&";
			String first_header = "first="+first+"&";
			if(first == -1) first_header = "";
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users/follows?"+from_id+first_header, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray jsonArray = (JSONArray) parser.parse(jsonfile.get("data").toString());
			
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(jsonArray.get(i).toString());
				
				TwitchUser cUser = getUser(client_id, app_access_token, "login="+cJson.get("to_login")+"&");
				list.add(cUser);
			}
			
			return list;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}

}
