package com.guess.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.guess.song.model.entity.UserInfo;
import com.guess.song.repository.UserInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserInfoRepository userRep;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	public String loginFail(String username) {
		if(userRep.findByUsername(username) == null) {
			return "username";
		}else {
			return "password";
		}
	}
	
	@SuppressWarnings("finally")
	public int join(UserInfo userInfoParam) {
		int result = -1;
		String beforePw = userInfoParam.getPassword();
		String afterPw = bcrypt.encode(beforePw);
		userInfoParam.setPassword(afterPw);
		userInfoParam.setRole("user");
		try {
			UserInfo userInfo = userRep.save(userInfoParam);
			if(userInfo != null) {
				result = 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			return result;
		}
		
		
		
	}
}
