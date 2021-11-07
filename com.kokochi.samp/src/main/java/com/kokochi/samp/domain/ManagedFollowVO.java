package com.kokochi.samp.domain;

import lombok.*;

@Getter @Setter
@ToString @AllArgsConstructor @NoArgsConstructor
public class ManagedFollowVO {
	private String id;
	private String user_id;
	private String to_user;
}
