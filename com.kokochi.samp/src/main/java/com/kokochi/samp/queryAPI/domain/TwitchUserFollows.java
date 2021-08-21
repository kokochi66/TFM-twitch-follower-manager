package com.kokochi.samp.queryAPI.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @NoArgsConstructor
@AllArgsConstructor
public class TwitchUserFollows {
	private String from_id = "";
	private String from_login = "";
	private String from_name = "";
	private String to_id = "";
	private String to_name = "";
	private Date followed_at = new Date();
	private int total = 0;
	private String nextPage = "";
}
