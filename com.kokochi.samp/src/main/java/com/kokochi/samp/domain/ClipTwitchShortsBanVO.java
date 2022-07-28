package com.kokochi.samp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ClipTwitchShortsBanVO {
    private Long id;
    private String user_id;
    private String ban_clip;
}
