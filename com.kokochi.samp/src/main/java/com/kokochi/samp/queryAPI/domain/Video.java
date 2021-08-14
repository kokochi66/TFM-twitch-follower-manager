package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Video {
	private String id;
	private String stream_id;
	private String user_id;
	private String user_name;
	private String title;
	private String description;
	private Date created_at;
	private Date published_at;
	private String url;
	private String thumbnail_url;
	private String viewable;
	private int view_count;
	private String language;
	private String type;
	private String duration;
	private JSONArray muted_segments;
	private String nextPage;
	private String profile_url;
	
	public JSONObject parseToJSONObject() {
		JSONObject res = new JSONObject();
		res.put("user_id", this.user_id);
		res.put("user_name", this.user_name);
		res.put("title", this.title);
		res.put("description", this.description);
		res.put("created_at", this.created_at.toString());
		res.put("url", this.url);
		res.put("thumbnail_url", this.thumbnail_url);
		res.put("view_count", this.view_count);
		res.put("language", this.language);
		res.put("nextPage", this.nextPage);
		res.put("profile_url", this.profile_url);
		
		return res;
	}
}
