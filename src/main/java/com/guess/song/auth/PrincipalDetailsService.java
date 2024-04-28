package com.guess.song.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.guess.song.model.entity.UserInfo;
import com.guess.song.model.param.UserInfoParam;

@Service
public class PrincipalDetailsService implements UserDetailsService{

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(username);
		userInfo.setRole("role_user");
		return new PrincipalDetails(userInfo);
	}
	

}
