package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Clips {
	private String id;
	private String url;
	private String embed_url;
	private String broadcaster_id;
	private String broadcaster_name;
	private String creator_id;
	private String creator_name;
	private String video_id;
	private String game_id;
	private String language;
	private String title;
	private int view_count;
	private Date created_at;
	private String thumbnail_url;
	private float duration;
	private String nextPage;
	private String profile_url;
	
	public Video clipsToVideo() {
		Video v = new Video();
		v.setUser_id(this.broadcaster_id);
		v.setUser_name(this.broadcaster_name);
		v.setTitle(this.title);
		v.setUrl(this.url);
		v.setThumbnail_url(this.thumbnail_url);
		v.setLanguage(this.language);
		v.setNextPage(this.nextPage);
		v.setProfile_url(profile_url);
		return v;
	}
}
