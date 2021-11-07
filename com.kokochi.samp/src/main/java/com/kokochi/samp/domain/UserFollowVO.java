package com.kokochi.samp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter
@ToString
public class UserFollowVO {
    private String id;
    private String from_id;
    private String to_id;
    private Date followed_at;
}
