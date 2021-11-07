package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

import com.kokochi.samp.domain.UserTwitchVO;
import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class TwitchUser {
	
	private String broadcaster_type = "";
	private String description = "";
	private String display_name = "";
	private String id = "";
	private String login = "";
	private String offline_image_url = "";
	private String profile_image_url = "";
	private String type = "";
	private int view_count = 0;
	private String email = "";
	private Date created_at = new Date();
	private boolean
			isManaged = false;
	
	public JSONObject TwitchUserToJSON() {
		JSONObject j = new JSONObject();
		j.put("description", this.description);
		j.put("display_name", this.display_name);
		j.put("id", this.id);
		j.put("login", this.login);
		j.put("offline_image_url", this.offline_image_url);
		j.put("profile_image_url", this.profile_image_url);
		j.put("type", this.type);
		j.put("view_count", this.view_count);
		j.put("email", this.email);
		j.put("created_at", this.created_at.toString());
		j.put("isManaged", this.isManaged);
		return j;
	}

	public UserTwitchVO toUserTwitchVO() {
		UserTwitchVO userTwitchVO = new UserTwitchVO();
		if(broadcaster_type != null) userTwitchVO.setBroadcaster_type(broadcaster_type);
		if(description != null) userTwitchVO.setDescription(description);
		if(display_name != null) userTwitchVO.setDisplay_name(display_name);
		if(id != null) userTwitchVO.setId(id);
		if(login != null) userTwitchVO.setLogin(login);
		if(offline_image_url != null) userTwitchVO.setOffline_image_url(offline_image_url);
		if(profile_image_url != null) userTwitchVO.setProfile_image_url(profile_image_url);
		if(type != null) userTwitchVO.setType(type);
		userTwitchVO.setView_count(view_count);
		if(email != null) userTwitchVO.setEmail(email);
		if(created_at != null) userTwitchVO.setCreated_at(created_at);
		userTwitchVO.setIsManaged(isManaged);
		return userTwitchVO;
	}
}
