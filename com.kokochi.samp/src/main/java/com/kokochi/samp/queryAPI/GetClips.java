package com.kokochi.samp.queryAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.kokochi.samp.domain.ClipTwitchVO;
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
import com.kokochi.samp.domain.ManagedFollowVO;
import com.kokochi.samp.queryAPI.domain.Clips;
import com.kokochi.samp.queryAPI.domain.TwitchUser;

public class GetClips {
	
	public Clips getClipsOneByUserId(String client_id, String access_Token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		List<Clips> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/clips?first=1&"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
			if(data.size() <= 0) return null; 
			JSONObject cJson = (JSONObject) parser.parse(data.get(0).toString());
			Clips clip = gsonParser.fromJson(cJson.toString(), Clips.class);
			clip.setNextPage(pagination.get("cursor").toString());
//			System.out.println("GetVideo - GetClipsOneByUserId :: " + clip.toString());
			return clip;
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
	public List<Clips> getClipsByUserId(String client_id, String Access_Token, String user_id, String query) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", Access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		List<Clips> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/clips?broadcaster_id="+user_id+"&"+query, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				Clips clip = gsonParser.fromJson(cJson.toString(), Clips.class);
				if(pagination.get("cursor") != null) clip.setNextPage(pagination.get("cursor").toString());
				res.add(clip);
			}
			return res;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}

	public List<ClipTwitchVO> getClipsAll(String client_id, String Access_Token, String query) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", Access_Token);
		headers.add("Client-id", client_id);

		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();

		List<ClipTwitchVO> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/clips?"+query, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());

			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				ClipTwitchVO clip = gsonParser.fromJson(cJson.toString(), ClipTwitchVO.class);
				res.add(clip);
			}
			if(pagination.get("cursor") != null) {
				List<ClipTwitchVO> nextList = getClipsAll(client_id, Access_Token, query + "&after=" + pagination.get("cursor").toString());
				for (ClipTwitchVO clips : nextList) {
					res.add(clips);
				}
			}
			return res;


		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());

			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}

		return null;
	}
	
	public List<Clips> getClipsRecentByUsers(List<ManagedFollowVO> users, String client_id, String access_Token, String query) throws Exception {
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<Clips> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		GetStream streamGetter = new GetStream();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, -7);
		today = new Date(cal.getTimeInMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			for(int i=0;i<users.size();i++) {
				String c_user_id = users.get(i).getTo_user();
//				System.out.println(c_user_id);
				
				ResponseEntity<String> response = rt.exchange(
						"https://api.twitch.tv/helix/clips?&broadcaster_id="+c_user_id+"&started_at="+format.format(today)+"&"+query, HttpMethod.GET,
						entity, String.class);
				JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
				JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
				JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
				
				if(data.size() <= 0) continue; 	// 가져온 비디오 데이터가 없다면 null을 출력
				TwitchUser t_user = streamGetter.getUser(client_id, access_Token, "id="+c_user_id);
				for(int j=0;j<data.size();j++) {
					JSONObject c_json = (JSONObject) parser.parse(data.get(j).toString());
//					System.out.println(c_json.toJSONString());
					Clips c = gsonParser.fromJson(c_json.toString(), Clips.class);
					c.setProfile_url(t_user.getProfile_image_url());
					if(j == data.size()-1) c.setNextPage(pagination.get("cursor").toString());
					res.add(c);
				}
			}
			Collections.sort(res, (a,b) -> b.getView_count() - a.getView_count());
			return res;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
	public List<Clips> getClipsRecentByUser(String client_id, String access_Token, String user_id, String query) throws Exception {
		System.out.println("getClipsRecentByUser :: " + user_id+" "+query);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		ArrayList<Clips> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		GetStream streamGetter = new GetStream();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, -7);
		today = new Date(cal.getTimeInMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/clips?started_at="+format.format(today)+"&broadcaster_id="+user_id+"&"+query, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			TwitchUser t_user = streamGetter.getUser(client_id, access_Token, "id="+user_id);
			for(int j=0;j<data.size();j++) {
				JSONObject c_json = (JSONObject) parser.parse(data.get(j).toString());
				Clips c = gsonParser.fromJson(c_json.toString(), Clips.class);
				c.setProfile_url(t_user.getProfile_image_url());
				if(j == data.size()-1) c.setNextPage(pagination.get("cursor").toString());
				res.add(c);
			}
			return res;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
}
