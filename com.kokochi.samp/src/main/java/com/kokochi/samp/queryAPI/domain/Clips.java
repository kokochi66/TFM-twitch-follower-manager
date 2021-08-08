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
	private String boradcaster_id;
	private String broadcaster_name;
	private String vide_id;
	private String game_id;
	private String language;
	private String title;
	private int view_count;
	private Date created_at;
	private String thumbnail_url;
	private int duratrion;
	private String nextPage;

}
