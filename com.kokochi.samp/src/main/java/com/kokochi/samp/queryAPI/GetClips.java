package com.kokochi.samp.queryAPI;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.*;
import com.kokochi.samp.domain.ClipTwitchVO;
import com.sun.deploy.net.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import sun.net.www.http.HttpClient;

public class GetClips {

	public List<ClipTwitchVO> getClipsAll(String client_id, String Access_Token, String query) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", Access_Token);
		headers.add("Client-id", client_id);

		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();

		List<ClipTwitchVO> res = new ArrayList<>();
		JSONParser parser = new JSONParser();
		Gson gsonParser = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();

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
		Gson gsonParser = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();

		try {
			ResponseEntity<String> response = rt.exchange("https://api.twitch.tv/cy/players?"+query, HttpMethod.GET, entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());

			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				ClipTwitchVO clip = gsonParser.fromJson(cJson.toString(), ClipTwitchVO.class);

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

	public List<ClipTwitchVO> getClipsStreams(String client_id, String Access_Token, List<String> streams, String query) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", Access_Token);
		headers.add("Client-id", client_id);

		HttpEntity entity = new HttpEntity(headers);

		List<ClipTwitchVO> res = new ArrayList<>();
		List<ClipThread> threadList = new ArrayList<>();
		for (String stream : streams) {
			ClipThread thread = new ClipThread(stream, query, entity, res);
			threadList.add(thread);
			thread.start();
		}
		for (ClipThread clipThread : threadList) {
			clipThread.join();
		}
		return res;
	}

	private static class ClipThread extends Thread{
		String stream;
		String query;
		List<ClipTwitchVO> res;
		HttpEntity entity;

		public ClipThread(String stream, String query, HttpEntity entity, List<ClipTwitchVO> res) {
			this.stream = stream;
			this.query = query;
			this.res = res;
			this.entity = entity;
		}

		@Override
		public void run() {
			try {
				RestTemplate rt = new RestTemplate();
				Gson gsonParser = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
				JSONParser parser = new JSONParser();
				ResponseEntity<String> response = rt.exchange(
						"https://api.twitch.tv/helix/clips?" + query+"&broadcaster_id="+stream, HttpMethod.GET,
						entity, String.class);
//				System.out.println("TEST :: response :: " + response.getBody() +" "+ stream);
				JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
				JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
				JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());

				if (data.size() <= 0) return;    // 가져온 비디오 데이터가 없다면 null을 출력
				for (int i = 0; i < data.size(); i++) {
					JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
					ClipTwitchVO clip = gsonParser.fromJson(cJson.toString(), ClipTwitchVO.class);

					if (pagination.get("cursor") != null) clip.setNextPage(pagination.get("cursor").toString());
					res.add(clip);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
