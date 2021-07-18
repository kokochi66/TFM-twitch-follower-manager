package com.kokochi.samp.queryAPI;

import java.beans.JavaBean;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kokochi.samp.queryAPI.domain.Stream;
import com.kokochi.samp.service.TwitchKeyService;

@Component
public class GetStream {
	
	@Autowired
	private TwitchKeyService key;
	
	public ArrayList<Stream> getLiveStreams() throws Exception {
		
		System.out.println("livestreams");
		
		String client_id = key.read("client_id").getKey_value();
		
		System.out.println("generated id = "  + client_id);
		
//		GetToken tokenGenerator = new GetToken();
//		JSONObject jsonfile = tokenGenerator.GetAppAccessToken();
		
		return new ArrayList<Stream>();
	}

}
