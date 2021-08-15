package com.kokochi.samp.queryAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
			if(data.size() <= 0) return null;
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
	
	public boolean getRelativeFollow(Map<String, Integer> res, String client_id, String access_token, String to_id, String next, int count) throws Exception {
		if(count >= 1) return true;
		System.out.println("getRelativeFollow - count :: " + count);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users/follows?first=100&"+to_id+next, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				String c_from_id = cJson.get("from_id").toString();
				JSONObject from_pagination = new JSONObject();
				
				do {
					String from_next = from_pagination.containsKey("cursor") ? from_pagination.get("cursor").toString() : "";
					response = rt.exchange(
							"https://api.twitch.tv/helix/users/follows?first=100&from_id="+c_from_id+"&after="+from_next, HttpMethod.GET,
							entity, String.class);
					JSONObject from_jsonfile = (JSONObject) parser.parse(response.getBody());
					JSONArray from_data = (JSONArray) parser.parse(from_jsonfile.get("data").toString());
					from_pagination = (JSONObject) parser.parse(from_jsonfile.get("pagination").toString());
//					System.out.println("getRelativeFollow - from_data.size :: " + from_data.size());
					
					for(int j=0;j<from_data.size();j++) {
						JSONObject from_cJson = (JSONObject) parser.parse(from_data.get(j).toString());
						String c_to_id = from_cJson.get("to_id").toString();
//						System.out.println("getRelativeFollow - from_to_id :: " + c_to_id +" "+ j +" "+ from_data.size());
						if(!res.containsKey(c_to_id)) {
							res.put(c_to_id, 1);
							
						} else {
							res.replace(c_to_id, res.get(c_to_id) + 1);
						}
					}
					
				} while(from_pagination.containsKey("cursor"));
			}
			if(pagination.containsKey("cursor")) getRelativeFollow(res,client_id,access_token, to_id, "after="+pagination.get("cursor").toString(), count + 1);
			
			return true;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return false;
		}
		
		return true;
	}
}
