package com.kokochi.samp.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kokochi.samp.domain.MemberVO;
import com.kokochi.samp.domain.MemberAuthVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter
@ToString
public class UserDTO implements UserDetails {

	private String id;
	private String user_id;
	private String user_pwd;
	private String user_nickname;
	private String user_email;
	private String twitch_user_id;
	private String twitch_user_login;
	private String Oauth_token;
	private int enable;
	private List<String> authority;
	
	public UserDTO(MemberVO memberVO, List<MemberAuthVO> authList) {
		this.id = memberVO.getId();
		this.user_id = memberVO.getUser_id();
		this.user_pwd = memberVO.getUser_pwd();
		this.user_nickname = memberVO.getUser_nickname();
		this.user_email = memberVO.getUser_email();
		this.twitch_user_id = memberVO.getTwitch_user_id();
		this.twitch_user_login = memberVO.getTwitch_user_login();
		this.enable = memberVO.getEnable();
		this.Oauth_token = null;
		this.authority = new ArrayList<String>();
		
		for(int i=0;i<authList.size();i++)	authority.add(authList.get(i).getAuthority());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		ArrayList<SimpleGrantedAuthority> grants = new ArrayList<>();
		for(String role : authority) grants.add(new SimpleGrantedAuthority(role));
		
		return grants;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.user_pwd;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.user_id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enable == 1 ? true : false;
	}

}
