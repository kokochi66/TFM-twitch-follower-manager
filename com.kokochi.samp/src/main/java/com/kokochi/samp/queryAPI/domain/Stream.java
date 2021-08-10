package com.kokochi.samp.queryAPI.domain;

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
	private Date started_at;
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
		v.setUrl("https://www.twitch.tv/"+this.user_login);
		v.setThumbnail_url(this.thumbnail_url);
		v.setLanguage(this.language);
		v.setNextPage("0");
		return v;
	}

}
