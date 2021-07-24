package com.kokochi.samp.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
public class UserDTO implements UserDetails {
	
	private String user_id;
	private String user_pwd;
	private String user_nickname;
	private List<String> authority;
	
	public UserDTO(Member member, List<MemberAuth> authList) {
		
		this.user_id = member.getUser_id();
		this.user_pwd = member.getUser_pwd();
		this.user_nickname = member.getUser_nickname();
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
		return true;
	}

}