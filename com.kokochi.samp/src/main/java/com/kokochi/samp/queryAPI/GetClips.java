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

	public List<ClipTwitchVO> getClips(String client_id, String Access_Token, String query) throws Exception {
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
			return res;


		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
//			System.out.println("GetVideo - getOneVideoFromId " + exceptionMessage.toJSONString());

			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}

		return null;
	}


}
