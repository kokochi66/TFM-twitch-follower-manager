package com.kokochi.samp.queryAPI.innerProcess;

import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class PostQuery {
	private String uri = "http://localhost:8080";
	// 서버 내부 자체적인 POST 쿼리가 필요한 경우에 사용하는 메소드
	// 서버의 uri값에 따라 쿼리가 변화되기 때문에, 사이트 주소가 변경된다면 uri값도 수동으로 변경 해 주어야 한다.
	
	public void initManagedFollow(String twitch_user_id, String user_id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("twitch_user_id", twitch_user_id);
		headers.add("user_id", user_id);
		JSONParser parser = new JSONParser();
		
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate rt = new RestTemplate();
		try {
			ResponseEntity<String> response = rt.exchange(
					uri + "/query/request/initFollow", HttpMethod.POST, entity, String.class);
			
		} catch (HttpStatusCodeException  e) {
			System.out.println("PostQuery : initManagedFollow - error :: " + e);
		}
	}

}
