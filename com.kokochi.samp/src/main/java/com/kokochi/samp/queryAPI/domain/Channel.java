package com.kokochi.samp.queryAPI.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @NoArgsConstructor
@AllArgsConstructor
public class Channel {
	private String broadcaster_id;
	private String broadcaster_login;
	private String broadcaster_name;
	private String broadcaster_language;
	private String game_id;
	private String game_name;
	private String title;
	private int delay;
}
