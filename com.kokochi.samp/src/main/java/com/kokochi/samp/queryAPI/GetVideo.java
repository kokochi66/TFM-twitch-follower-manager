package com.kokochi.samp.queryAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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
import com.kokochi.samp.queryAPI.domain.Video;

public class GetVideo {
	
	private JSONParser parser = new JSONParser();
	private Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
	
	public Video getOneVideoFromId(String client_id, String OAuth_Token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", OAuth_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/videos?first=1&sort=time&"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			JSONObject pagination = (JSONObject) parser.parse(jsonfile.get("pagination").toString());
			
//			System.out.println("GetVideo - getOneVideoFromId " + jsonfile.toJSONString() +" "+ user_id);
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			JSONObject cJson = (JSONObject) parser.parse(data.get(0).toString());
			
			Video video = gsonParser.fromJson(cJson.toString(), Video.class);
			video.setNextPage(pagination.get("cursor").toString());
			
			return video;
			
			
		} catch (HttpStatusCodeException  e) {
			JSONObject exceptionMessage = (JSONObject) parser.parse(e.getResponseBodyAsString());
			
			if(exceptionMessage.get("status").toString().equals("401")) return null;
		}
		
		return null;
	}
	
	public ArrayList<Video> getRecentVideoFromUsers(ArrayList<TwitchUser> users, String client_id, String OAuth_Token, int limit) throws Exception { 
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		ArrayList<Video> res = new ArrayList<>();
		
		PriorityQueue<Video> pq = new PriorityQueue<>((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()));
		for(int i=0;i<users.size();i++) {
			Video v = getOneVideoFromId(client_id, OAuth_Token, "user_id="+users.get(i).getId());
			if(v != null) pq.add(v);
			if(pq.size() > limit) pq.poll();
		}
		// 최초로 각 스트리머들의 가장 최근 영상들을 pq에 삽입한다.
		
		for(int i=0;i<limit;i++) {
			Queue<Video> que = new LinkedList<>();
			while(!pq.isEmpty()) que.add(pq.poll());
			while(!que.isEmpty()) {
				Video v = que.poll();
				pq.add(v);
				if(pq.size() > limit) pq.poll();
				
				Video vc = getOneVideoFromId(client_id, OAuth_Token, "after="+v.getNextPage()+"&user_id="+users.get(i).getId());
				if(vc != null) pq.add(vc);
				if(pq.size() > limit) pq.poll();
			}
		}	// 가장 깊은 값을 찾기 위해서 limit 만큼 pq값을 돌린다.
		
		while(!pq.isEmpty()) res.add(pq.poll()); // pq에서 결과 리스트값으로 값을 넣는다.
		return res;
	}

}
