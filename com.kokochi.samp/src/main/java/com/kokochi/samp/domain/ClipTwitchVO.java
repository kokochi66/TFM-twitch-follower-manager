package com.kokochi.samp.domain;

import com.kokochi.samp.queryAPI.domain.Video;
import lombok.*;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClipTwitchVO {
	private String id;
	private String url;
	private String user_id;
	private String embed_url;
	private String broadcaster_id;
	private String broadcaster_name;
	private String creator_id;
	private String creator_name;
	private String video_id;
	private String game_id;
	private String language;
	private String title;
	private Integer view_count;
	private Date created_at;
	private String thumbnail_url;
	private Float duration;
	private String nextPage;
	private String profile_image_url;

	private Integer limit;
	private Long points;

	
	public Video clipsToVideo() {
		Video v = new Video();
		v.setUser_id(this.broadcaster_id);
		v.setUser_name(this.broadcaster_name);
		v.setView_count(this.view_count);
		v.setTitle(this.title);
		v.setUrl(this.url);
		v.setThumbnail_url(this.thumbnail_url);
		v.setLanguage(this.language);
		v.setCreated_at(this.created_at);
		v.setNextPage(this.nextPage);
		v.setProfile_url(this.profile_image_url);
		return v;
	}
	
	public JSONObject clipsToJSON() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatStr = new SimpleDateFormat("yyyy년 MM월dd일 HH시 mm분");
		JSONObject j = new JSONObject();
		j.put("user_id", this.broadcaster_id);
		j.put("user_name", this.broadcaster_name);
		j.put("title", this.title);
		j.put("url", this.url);
		j.put("view_count", this.view_count);
		j.put("points ", format.format(this.created_at));
		j.put("created_at", formatStr.format(this.created_at));
		j.put("thumbnail_url", this.thumbnail_url);
		j.put("language", this.language);
		j.put("nextPage", this.nextPage);
		j.put("points", this.points);
		j.put("profile_image_url", this.profile_image_url);
		return j;
	}
}
