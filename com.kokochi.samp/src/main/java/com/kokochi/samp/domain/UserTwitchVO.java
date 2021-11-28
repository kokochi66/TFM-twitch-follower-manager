package com.kokochi.samp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
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

	public JSONObject parseToJSON() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatStr = new SimpleDateFormat("yyyy년 MM월dd일 HH시 mm분");
		JSONObject jsonObject = new JSONObject();
		if(broadcaster_type!=null) jsonObject.put("broadcaster_type",this.broadcaster_type);
		if(description!=null) jsonObject.put("description",this.description);
		if(display_name!=null) jsonObject.put("display_name",this.display_name);
		if(id!=null) jsonObject.put("id",this.id);
		if(login!=null) jsonObject.put("login",this.login);
		if(description!=null) jsonObject.put("description",this.description);
		if(offline_image_url!=null) jsonObject.put("offline_image_url",this.offline_image_url);
		if(profile_image_url!=null) jsonObject.put("profile_image_url",this.profile_image_url);
		if(type!=null) jsonObject.put("type",this.type);
		if(view_count!=null) jsonObject.put("view_count",this.view_count);
		if(email!=null) jsonObject.put("email",this.email);
		if(offline_image_url!=null) jsonObject.put("created_at",formatStr.format(this.created_at));
		if(managed_id!=null) jsonObject.put("managed_id",this.managed_id);
		if(isManaged!=null) jsonObject.put("isManaged",this.isManaged);
		return jsonObject;
	}
}
