package com.kokochi.samp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @AllArgsConstructor
public class ManagedFollowVO {
	private String id;
	private String userId;
	private String toUser;
}
