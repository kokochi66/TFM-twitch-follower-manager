package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class TwitchUser {
	
	private String broadcaster_type;
	private String description;
	private String display_name;
	private String id;
	private String login;
	private String offline_image_url;
	private String profile_image_url;
	private String type;
	private int view_count;
	private String email;
	private Date created_at;
	private boolean checking_managed;
}
