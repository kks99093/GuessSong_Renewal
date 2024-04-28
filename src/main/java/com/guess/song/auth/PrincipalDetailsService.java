package com.guess.song.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.guess.song.model.entity.UserInfo;
import com.guess.song.repository.UserInfoRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	private UserInfoRepository userInfoRep;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userEntity = userInfoRep.findByUsername(username);
		if(userEntity == null) {
			throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
		}
		
		return new PrincipalDetails(userEntity);
	}
	

}
