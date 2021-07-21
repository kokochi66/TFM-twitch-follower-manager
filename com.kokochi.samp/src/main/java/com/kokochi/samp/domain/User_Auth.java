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
@Table(name = "user_auth")
public class User_Auth {
	
	@Id
	@Column(name = "user_id")
	private String user_id;
	
	@Column(name = "authority")
	private String authority;
	
	public User_Auth(String user_id2, String authority2) {
		// TODO Auto-generated constructor stub
	}
	
}
