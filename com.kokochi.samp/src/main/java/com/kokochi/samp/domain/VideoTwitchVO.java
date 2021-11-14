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
	private String profile_image_url;
	private Boolean isManaged;

	private Integer limit;	// 쿼리용
	private Date points;


	public JSONObject parseToJSON() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat formatStr = new SimpleDateFormat("yyyy년 MM월dd일 HH시 mm분");
		JSONObject jsonObject = new JSONObject();
		if(id!=null) jsonObject.put("id",this.id);
		if(stream_id!=null) jsonObject.put("stream_id",this.stream_id);
		if(user_id!=null) jsonObject.put("user_id",this.user_id);
		if(user_name!=null) jsonObject.put("user_name",this.user_name);
		if(title!=null) jsonObject.put("title",this.title);
		if(description!=null) jsonObject.put("description",this.description);
		if(created_at!=null) jsonObject.put("created_at",formatStr.format(this.created_at));
		if(points!=null) jsonObject.put("points",format.format(this.created_at));
		if(published_at!=null) jsonObject.put("published_at",format.format(this.published_at));
		if(url!=null) jsonObject.put("url",this.url);
		if(thumbnail_url!=null) jsonObject.put("thumbnail_url",this.thumbnail_url);
		if(viewable!=null) jsonObject.put("viewable",this.viewable);
		if(view_count!=null) jsonObject.put("view_count",this.view_count);
		if(type!=null) jsonObject.put("type",this.type);
		if(duration!=null) jsonObject.put("duration",this.duration);
		if(nextPage!=null) jsonObject.put("nextPage",this.nextPage);
		if(profile_image_url!=null) jsonObject.put("profile_image_url",this.profile_image_url);
		if(isManaged!=null) jsonObject.put("isManaged",this.isManaged);
		return jsonObject;
	}

}
