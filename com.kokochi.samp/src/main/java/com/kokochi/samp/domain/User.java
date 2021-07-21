package com.kokochi.samp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "user_id")
	private String user_id;
	
	@Column(name = "user_pwd")
	private String user_pwd;
	
	@Column(name = "user_email")
	private String user_email;
	
	@Column(name = "user_nickname")
	private String user_nickname;
	
	@Column(name = "twitch_user_id")
	private String twitch_user_id;
	
	
}
