package com.guess.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guess.song.repository.UserInfoRepository;

@Service
public class UserService {
	
	@Autowired
	private UserInfoRepository userRep;
	
	
	public String loginFail(String username) {
		if(userRep.findByUsername(username) == null) {
			return "username";
		}else {
			return "password";
		}
	}
}
