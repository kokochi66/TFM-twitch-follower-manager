package com.kokochi.samp.queryAPI.domain;

import java.time.LocalDateTime;
import java.util.Date;

import com.kokochi.samp.domain.ClipTwitchVO;
import org.json.simple.JSONObject;

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
	private LocalDateTime created_at;
	private String thumbnail_url;
	private float duration;
	private String nextPage;
	private String profile_url;
	
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
		v.setProfile_url(this.profile_url);
		return v;
	}
	
	public JSONObject clipsToJSON() {
		JSONObject j = new JSONObject();
		j.put("broadcaster_id", this.broadcaster_id);
		j.put("broadcaster_name", this.broadcaster_name);
		j.put("title", this.title);
		j.put("url", this.url);
		j.put("view_count", this.view_count);
		j.put("created_at", this.created_at.toString());
		j.put("thumbnail_url", this.thumbnail_url);
		j.put("language", this.language);
		j.put("nextPage", this.nextPage);
		j.put("profile_url", this.profile_url);
		return j;
	}
	public ClipTwitchVO parseToClipTwitchVO() {
		ClipTwitchVO clipTwitchVO = new ClipTwitchVO();
		if(this.id != null) clipTwitchVO.setId(this.id);
		if(this.url != null) clipTwitchVO.setUrl(this.url);
		if(this.embed_url != null) clipTwitchVO.setEmbed_url(this.embed_url);
		if(this.broadcaster_id != null) clipTwitchVO.setBroadcaster_id(this.broadcaster_id);
		if(this.broadcaster_name != null) clipTwitchVO.setBroadcaster_name(this.broadcaster_name);
		if(this.creator_id != null) clipTwitchVO.setCreator_id(this.creator_id);
		if(this.creator_name != null) clipTwitchVO.setCreator_name(this.creator_name);
		if(this.video_id != null) clipTwitchVO.setVideo_id(this.video_id);
		if(this.game_id != null) clipTwitchVO.setGame_id(this.game_id);
		if(this.language != null) clipTwitchVO.setLanguage(this.language);
		if(this.title != null) clipTwitchVO.setTitle(this.title);
		clipTwitchVO.setView_count(this.view_count);
		if(this.created_at != null) clipTwitchVO.setCreated_at(this.created_at);
		if(this.thumbnail_url != null) clipTwitchVO.setThumbnail_url(this.thumbnail_url);
		clipTwitchVO.setDuration(this.duration);
		if(this.nextPage != null) clipTwitchVO.setNextPage(this.nextPage);
		if(this.profile_url != null) clipTwitchVO.setProfile_image_url(this.profile_url);
		return clipTwitchVO;
	}
}
