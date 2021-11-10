package com.kokochi.samp.domain;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VideoTwitchVO {
	private String id;
	private String stream_id;
	private String user_id;
	private String user_name;
	private String title ;
	private String description;
	private Date created_at;
	private Date published_at;
	private String url;
	private String thumbnail_url;
	private String viewable;
	private Integer view_count;
	private String language;
	private String type;
	private String duration;
	private String nextPage;
	private String profile_url;
	private Boolean isManaged;

}
