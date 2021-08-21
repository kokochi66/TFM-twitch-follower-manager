package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

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
	private boolean isManaged = false;
	
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
}
