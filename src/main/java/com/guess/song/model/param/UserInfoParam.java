package com.guess.song.model.param;

import lombok.Data;

@Data
public class UserInfoParam {

	private Integer userPk;
	
	private String userName;
	
	private String sessionId;
	
	private String role;
	
	private Integer userRole;
}
