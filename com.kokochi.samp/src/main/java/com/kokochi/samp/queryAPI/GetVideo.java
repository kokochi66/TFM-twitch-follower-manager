package com.kokochi.samp.queryAPI;

import java.util.ArrayList;
import java.util.List;

import com.kokochi.samp.domain.VideoTwitchVO;
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
import com.kokochi.samp.queryAPI.domain.TwitchUser;
import com.kokochi.samp.queryAPI.domain.Video;

public class GetVideo {
	
	public Video getOneVideoFromId(String client_id, String access_Token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/videos?first=1&sort=time&"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
			
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			JSONObject cJson = (JSONObject) parser.parse(data.get(0).toString());
//			System.out.println("GetVideo - getOneVideoFromId " + cJson.toString() +" "+ user_id);
			
			
			Video video = gsonParser.fromJson(cJson.toString(), Video.class);
			video.setNextPage(pagination.get("cursor").toString());
			
			return video;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
	public ArrayList<Video> getVideoFromId(String client_id, String access_Token, String user_id, int limit) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		ArrayList<Video> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/videos?first="+limit+"&sort=time&"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				Video video = gsonParser.fromJson(cJson.toString(), Video.class);
				video.setNextPage(pagination.get("cursor").toString());
				res.add(video);
			}
			return res;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return res;
	}

	public ArrayList<VideoTwitchVO> getRecentVideo(String client_id, String access_token, String query) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_token);
		headers.add("Client-id", client_id);

		GetStream streamGetter = new GetStream();
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		ArrayList<VideoTwitchVO> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

		try {
//				System.out.println("GetVideo - userId =  " + c_user_id);
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/videos"+query, HttpMethod.GET, entity, String.class);
//				System.out.println("getRecentVideoFromUsers :: " + response.getBody());
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력

			for(int j=0;j<data.size();j++) {
//					System.out.println("GetVideo - getRecentVideoFromUsers " + data.get(j).toString() +" "+j+" "+data.size());
				JSONObject cJson = (JSONObject) parser.parse(data.get(j).toString());
				VideoTwitchVO v = gsonParser.fromJson(cJson.toString(), VideoTwitchVO.class);
				if(j == data.size()-1  && pagination.containsKey("cursor")) v.setNextPage(pagination.get("cursor").toString());
				res.add(v);
			}
//			Collections.sort(res, (a,b) -> b.getCreated_at().compareTo(a.getCreated_at()));
			return res;


		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());

			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return res;
	}

	
	public ArrayList<Video> getRecentVideoFromUsers(String client_id, String access_token,
                                                    List<ManagedFollowVO> users, String query) throws Exception {
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_token);
		headers.add("Client-id", client_id);
		
		GetStream streamGetter = new GetStream();
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		ArrayList<Video> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			for(int i=0;i<users.size();i++) {
				String c_user_id = users.get(i).getTo_user();
//				System.out.println("GetVideo - userId =  " + c_user_id);
				ResponseEntity<String> response = rt.exchange(
						"https://api.twitch.tv/helix/videos?user_id="
								+c_user_id+"&sort=time&"+query, HttpMethod.GET, entity, String.class);
//				System.out.println("getRecentVideoFromUsers :: " + response.getBody());
				JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
				JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
				JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
				if(data.size() <= 0) continue; 	// 가져온 비디오 데이터가 없다면 null을 출력
				
				TwitchUser c_tu = streamGetter.getUser(client_id, access_token, "id="+c_user_id);
				
				for(int j=0;j<data.size();j++) {
//					System.out.println("GetVideo - getRecentVideoFromUsers " + data.get(j).toString() +" "+j+" "+data.size());
					JSONObject cJson = (JSONObject) parser.parse(data.get(j).toString());
					Video v = gsonParser.fromJson(cJson.toString(), Video.class);
					v.setProfile_url(c_tu.getProfile_image_url());
					if(j == data.size()-1  && pagination.containsKey("cursor")) v.setNextPage(pagination.get("cursor").toString());
					res.add(v);
				}
			}
//			Collections.sort(res, (a,b) -> b.getCreated_at().compareTo(a.getCreated_at()));
			return res;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return res;
	}
	
	public ArrayList<Video> getRecentVideoFromUserNext(String client_id, String access_token, String user_id, String query) throws Exception { 
		System.out.println("getRecentVideoFromUserNext - " + user_id +" " + query);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_token);
		headers.add("Client-id", client_id);
		
		GetStream streamGetter = new GetStream();
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		ArrayList<Video> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/videos?user_id="
							+user_id+"&sort=time&"+query, HttpMethod.GET, entity, String.class);
//			System.out.println("getRecentVideoFromUserNext - " + response.getBody());
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			if(data.size() <= 0) return res; 	// 가져온 비디오 데이터가 없다면 null을 출력
			
			TwitchUser c_tu = streamGetter.getUser(client_id, access_token, "id="+user_id);
			for(int i=0;i<data.size();i++) {
//				System.out.println("getRecentVideoFromUserNext - " + data.get(i).toString()+" "+i+" "+data.size());
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				
				Video v = gsonParser.fromJson(cJson.toString(), Video.class);
				v.setProfile_url(c_tu.getProfile_image_url());
				if(i == data.size()-1 && pagination.containsKey("cursor")) v.setNextPage(pagination.get("cursor").toString());
				res.add(v);
			}
//			System.out.println("getRecentVideoFromUserNext - res.size :: "+res.size());
			return res;
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		return res;
	}
	
}
