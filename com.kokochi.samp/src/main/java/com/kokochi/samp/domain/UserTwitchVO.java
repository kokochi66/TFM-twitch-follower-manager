package com.kokochi.samp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONObject;

import java.util.Date;

@Getter @Setter
@ToString
public class UserTwitchVO {
	private String broadcaster_type;
	private String description;
	private String display_name;
	private String id;
	private String login;
	private String offline_image_url;
	private String profile_image_url;
	private String type;
	private Integer view_count;
	private String email;
	private Date created_at;
	private String managed_id;
	private Boolean isManaged;
}
