package com.kokochi.samp.security.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kokochi.samp.domain.Member;
import com.kokochi.samp.domain.MemberAuth;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	private Map<String, Member> userMap = new HashMap<>();
	private Map<String, List<MemberAuth>> permMap = new HashMap<>();
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Member member = findUserInfo(userId);
		if (member == null)
			throw new UsernameNotFoundException(userId);

		List<MemberAuth> perms = loadPermission(member.getUser_id());
		List<GrantedAuthority> auth = new ArrayList<>();
		for (MemberAuth perm : perms) {
			auth.add(new SimpleGrantedAuthority(perm.getAuthority()));
		}
		return new User(userId, member.getUser_pwd(), auth);
	}
	
	private Member findUserInfo(String username) {
		return userMap.get(username);
	}

	private List<MemberAuth> loadPermission(String username) {
		return permMap.get(username);
	}

	public CustomUserDetailsService() {
		userMap.put("system", new Member("root", "1234", "root@gg", "root", "55"));
		permMap.put("system", Arrays.asList(new MemberAuth("root", "MEMBER")));
	}

}
