package com.kokochi.samp.queryAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.kokochi.samp.domain.ManagedFollow;
import com.kokochi.samp.queryAPI.domain.Clips;
import com.kokochi.samp.queryAPI.domain.Video;

public class GetVideo {
	
	private JSONParser parser = new JSONParser();
	private Gson gsonParser = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
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
	
	public ArrayList<Video> getRecentVideoFromUsers(List<ManagedFollow> users, String client_id, String OAuth_Token, int limit) throws Exception { 
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		ArrayList<Video> res = new ArrayList<>();
		
		PriorityQueue<Video> pq = new PriorityQueue<>((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()));
		for(int i=0;i<users.size();i++) {
			Video v = getOneVideoFromId(client_id, OAuth_Token, "user_id="+users.get(i).getTo_user());
			if(v != null) pq.add(v);
			if(pq.size() > limit) pq.poll();
		}
		// 최초로 각 스트리머들의 가장 최근 영상들을 pq에 삽입한다.
		
		Queue<Video> que = new LinkedList<>();
		while(!pq.isEmpty()) que.add(pq.poll());
		
		for(int i=0;i<limit;i++) {
			if(que.size() < limit - i) continue;
			Video v = que.poll();
			if(v != null) pq.add(v);
			if(pq.size() > limit) pq.poll();
			for(int j=0;j<i;j++) {
				Video vc = getOneVideoFromId(client_id, OAuth_Token, "after="+v.getNextPage()+"&user_id="+v.getUser_id());
				if(vc != null) {
					v = vc;
					pq.add(v);
				}
				if(pq.size() > limit) pq.poll();
			}
		}
		
		while(!pq.isEmpty()) res.add(0, pq.poll()); // pq에서 결과 리스트값으로 값을 넣는다.
		return res;
	}
	
	public ArrayList<Video> getRecentVideoFromUsersToNext(Map<String,String> serviceMap, List<ManagedFollow> users, String client_id, String OAuth_Token, int limit) throws Exception { 
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		ArrayList<Video> res = new ArrayList<>();
		
		PriorityQueue<Video> pq = new PriorityQueue<>((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()));
		for(int i=0;i<users.size();i++) {
			String toUserId = users.get(i).getTo_user();
			Video v;
			if(serviceMap.containsKey(toUserId)) {
				v = getOneVideoFromId(client_id, OAuth_Token, "after="+serviceMap.get(toUserId)+"&user_id="+toUserId);
//				System.out.println("GetVideo - getRecentVideoFromUsersToNext :: " + v.toString());
			}
			else v = getOneVideoFromId(client_id, OAuth_Token, "user_id="+toUserId);
			if(v != null) pq.add(v);
			if(pq.size() > limit) pq.poll();
		}
		// 최초로 각 스트리머들의 가장 최근 영상들을 pq에 삽입한다.
		
		Queue<Video> que = new LinkedList<>();
		while(!pq.isEmpty()) que.add(pq.poll());
		
		for(int i=0;i<limit;i++) {
			if(que.size() < limit - i) continue;
			Video v = que.poll();
			if(v != null) pq.add(v);
			if(pq.size() > limit) pq.poll();
			for(int j=0;j<i;j++) {
				Video vc = getOneVideoFromId(client_id, OAuth_Token, "after="+v.getNextPage()+"&user_id="+v.getUser_id());
				if(vc != null) {
					v = vc;
					pq.add(v);
				}
				if(pq.size() > limit) pq.poll();
			}
		}
		
		while(!pq.isEmpty()) res.add(0, pq.poll()); // pq에서 결과 리스트값으로 값을 넣는다.
		return res;
	}
	
	public Clips getClipsOneByUserId(String client_id, String access_Token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		List<Clips> res = new ArrayList<>();
		
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
	
	public List<Clips> getClipsByUserId(String client_id, String Access_Token, String user_id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", Access_Token);
		headers.add("Client-id", client_id);
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		
		List<Clips> res = new ArrayList<>();
		
		try {
			ResponseEntity<String> response = rt.exchange(
					"https://api.twitch.tv/helix/clips?"+user_id, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonfile = (JSONObject) parser.parse(response.getBody());
			JSONArray data = (JSONArray) parser.parse(jsonfile.get("data").toString());
			
			if(data.size() <= 0) return null; 	// 가져온 비디오 데이터가 없다면 null을 출력
			for(int i=0;i<data.size();i++) {
				JSONObject cJson = (JSONObject) parser.parse(data.get(i).toString());
				
				Clips clip = gsonParser.fromJson(cJson.toString(), Clips.class);
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
	
	public List<Clips> getClipsRecentByUsers(List<ManagedFollow> users, String client_id, String access_Token, int limit) throws Exception {
		if(users.size() <= 0) return null; // users의 크기가 1 이상이어야함.
		ArrayList<Clips> res = new ArrayList<>();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, -7);
		today = new Date(cal.getTimeInMillis());

		
		PriorityQueue<Clips> pq = new PriorityQueue<>((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()));
		for(int i=0;i<users.size();i++) {
//			System.out.println("GetVideo - getClipsRecentByUsers :: " + "broadcaster_id="+users.get(i).getTo_user());
			Clips c = getClipsOneByUserId(client_id, access_Token, "broadcaster_id="+users.get(i).getTo_user()+"&started_at="+format.format(today));
			if(c != null) pq.add(c);
			if(pq.size() > limit) pq.poll();
		}
		// 최초로 각 스트리머들의 가장 최근 영상들을 pq에 삽입한다.
		
		Queue<Clips> que = new LinkedList<>();
		while(!pq.isEmpty()) que.add(pq.poll());
		
		for(int i=0;i<limit;i++) {
			if(que.size() < limit - i) continue;
			Clips c = que.poll();
			if(c != null) pq.add(c);
			if(pq.size() > limit) pq.poll();
			for(int j=0;j<i;j++) {
				
				Clips cc = getClipsOneByUserId(client_id, access_Token, "after="+c.getNextPage()+"&broadcaster_id="+c.getBroadcaster_id()+"&started_at="+format.format(today));
				if(cc != null) { 
					c = cc;
					pq.add(c);
				}
				if(pq.size() > limit) pq.poll();
			}
		}
		
		while(!pq.isEmpty()) res.add(0, pq.poll()); // pq에서 결과 리스트값으로 값을 넣는다.
		return res;
	}
	
	
}
