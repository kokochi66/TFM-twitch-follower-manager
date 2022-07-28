package com.kokochi.samp.queryAPI.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
public class Stream {
	
	private String id;
	private String user_id;
	private String user_login;
	private String user_name;
	private String game_id;
	private String game_name;
	private String type;
	private String title;
	private int viewer_count;
	private LocalDateTime started_at;
	private String language;
	private String thumbnail_url;
	private String profile_image_url;
	private String[] tag_ids;
	private boolean is_mature;
	private String pagination;
	
	public Video StreamToVideo() {
		Video v = new Video();
		v.setUser_id(this.user_id);
		v.setUser_name(this.user_name);
		v.setTitle(this.title);
		v.setDescription("");
		v.setCreated_at(this.started_at);
		v.setUrl("https://www.twitch.tv/"+this.user_login);
		v.setThumbnail_url(this.thumbnail_url);
		v.setView_count(this.viewer_count);
		v.setLanguage(this.language);
		v.setNextPage("");
		v.setProfile_url(this.profile_image_url);
		return v;
	}
	
	public JSONObject StreamToJSON() {
		JSONObject j = new JSONObject();
		j.put("id", this.id);
		j.put("user_id", this.user_id);
		j.put("user_login", this.user_login);
		j.put("user_name", this.user_name);
		j.put("game_id", this.game_id);
		j.put("game_name", this.game_name);
		j.put("type", this.type);
		j.put("title", this.title);
		j.put("viewer_count", this.viewer_count);
		j.put("started_at", this.started_at.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
		j.put("language", this.language);
		j.put("thumbnail_url", this.thumbnail_url);
		j.put("profile_image_url", this.profile_image_url);
		return j;
	}
}
