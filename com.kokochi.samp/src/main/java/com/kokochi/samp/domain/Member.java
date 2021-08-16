package com.kokochi.samp.domain;

import java.util.List;

import com.kokochi.samp.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {
	private String user_id;
	private String user_pwd;
	private String user_email;
	private String user_nickname;
	private String twitch_user_id;
	private String twitch_user_login;
	private int enable;
}
