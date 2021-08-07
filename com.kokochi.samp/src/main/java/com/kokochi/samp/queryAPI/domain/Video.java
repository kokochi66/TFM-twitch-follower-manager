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
	private JSONObject muted_segments;
	private String nextPage;
}
