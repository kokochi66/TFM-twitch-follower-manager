package com.kokochi.samp.queryAPI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import com.kokochi.samp.domain.UserFollowVO;
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
import com.kokochi.samp.queryAPI.domain.TwitchUser;

public class GetFollow {

	public void requestGet() {
		String apiKey = "";

		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();

		headers.add("Authorization", apiKey);

		ResponseEntity<String> response = rt.exchange("사이퍼즈사이트Url/cy/players?nickname=꼬꼬치꼬치", HttpMethod.GET, entity, String.class);
		String body = response.getBody();
	}
	
	public ArrayList<TwitchUser> getFollowedList(String client_id, String app_access_token, String user_id, String first) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<TwitchUser> list = new ArrayList<>();
		JSONParser parser = new JSONParser();
		GetStream streamGetter = new GetStream();
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users/follows?"+user_id+first, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				TwitchUser cUser = streamGetter.getUser(client_id, app_access_token, "id="+cJson.get("to_id").toString());
				list.add(cUser);
			}
			return list;
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return null;
	}
	
	public ArrayList<UserFollowVO> getAllFollowedList(String client_id, String app_access_token, String user_id) throws Exception {
		// 특정 유저의 모든 팔로우 목록을 조회하는 쿼리이다.
		// 한번에 쿼리로 찾을 수 있는 팔로우 수는 100개까지이므로, 모든 팔로우 목록을 찾을 때 까지 nextPage를 반복하여 찾아야한다.
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		Gson gsonParser = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
		
		ArrayList<UserFollowVO> list = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			JSONObject pagination = new JSONObject();
			String nextPage = "";
			do {
				ResponseEntity<String> response = rt.exchange(
						"https://api.twitch.tv/helix/users/follows?"+user_id+"&first=100&"+nextPage, HttpMethod.GET,
						entity, String.class);
				JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
				JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
				pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
				if(pagination.containsKey("cursor")) nextPage = "after="+pagination.get("cursor").toString();
				
				for(int i=0;i<data.size();i++) {
					JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
					UserFollowVO userFollowVO = gsonParser.fromJson(cJson.toString(), UserFollowVO.class);
					list.add(userFollowVO);
				}
			} while(pagination.containsKey("cursor"));
			return list;
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return null;
	}


	public ArrayList<UserFollowVO> getAllFollowedListToFollowVO(String client_id, String app_access_token, String user_id) throws Exception {
		// 특정 유저의 모든 팔로우 목록을 조회하는 쿼리이다.
		// 한번에 쿼리로 찾을 수 있는 팔로우 수는 100개까지이므로, 모든 팔로우 목록을 찾을 때 까지 nextPage를 반복하여 찾아야한다.

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);

		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();

		ArrayList<UserFollowVO> list = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
		try {
			JSONObject pagination = new JSONObject();
			String nextPage = "";
			do {
				ResponseEntity<String> response = rt.exchange(
						"https://api.twitch.tv/helix/users/follows?"+user_id+"&first=100&"+nextPage, HttpMethod.GET,
						entity, String.class);
				JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
				JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
				pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
				if(pagination.containsKey("cursor")) nextPage = "after="+pagination.get("cursor").toString();

				for(int i=0;i<data.size();i++) {
					JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
					UserFollowVO userFollowVO = gsonParser.fromJson(cJson.toString(), UserFollowVO.class);
//					System.out.println("TEST :: 팔로우 데이터 :: " + userFollowVO.toString());
					list.add(userFollowVO);
				}
			} while(pagination.containsKey("cursor"));
			return list;
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return null;
	}
	
	public int getFollowedTotal(String client_id, String app_access_token, String user_id) throws Exception {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", app_access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		JSONParser parser = new JSONParser();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/users/follows?first=1&"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			
			return Integer.parseInt(jsonfile.get("total").toString());
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return -1;
		}
		
		return -1;
	}
	
	public boolean getRelativeFollow(Map<String, Integer> res, String client_id, String access_token, String to_id, String next, int count) throws Exception {
		if(count >= 1) return true;
//		System.out.println("getRelativeFollow - count :: " + count);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		JSONParser parser = new JSONParser();
		
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
