package com.kokochi.samp.queryAPI.domain;

import java.text.SimpleDateFormat;
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
	private String id = "";
	private String stream_id = "";
	private String user_id = "";
	private String user_name = "";
	private String title = "";
	private String description = "";
	private Date created_at = new Date();
	private Date published_at = new Date();
	private String url = "";
	private String thumbnail_url = "";
	private String viewable = "";
	private int view_count = 0;
	private String language = "";
	private String type = "";
	private String duration = "";
	private JSONArray muted_segments = new JSONArray();
	private String nextPage = "";
	private String profile_url = "";
	private boolean isManaged = false;
	
	public JSONObject parseToJSONObject() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatStr = new SimpleDateFormat("yyyy년 MM월dd일 HH시 mm분");
//		System.out.println("JSONObject - parseToJSONObject :: " + format.format(this.created_at));
		JSONObject res = new JSONObject();
		res.put("id", this.id);
		res.put("stream_id", this.stream_id);
		res.put("user_id", this.user_id);
		res.put("user_name", this.user_name);
		res.put("title", this.title);
		res.put("description", this.description);
		res.put("points", format.format(this.created_at));
		res.put("created_at", formatStr.format(this.created_at));
		res.put("url", this.url);
		res.put("thumbnail_url", this.thumbnail_url);
		res.put("view_count", this.view_count);
		res.put("language", this.language);
		res.put("nextPage", this.nextPage);
		res.put("profile_image_url", this.profile_url);
		res.put("isManaged", this.isManaged);
		
		return res;
	}
}
