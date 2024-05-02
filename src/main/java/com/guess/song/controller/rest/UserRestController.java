package com.guess.song.controller.rest;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.UserInfo;
import com.guess.song.service.BoardService;
import com.guess.song.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/rest/userNameChk")
	public int userNameChk(@RequestBody ObjectNode saveObj) throws UnsupportedEncodingException, JsonProcessingException, IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper();
		UserInfo userInfo = mapper.treeToValue(saveObj.get("userInfo"), UserInfo.class);
		GameRoom gameRoom = mapper.treeToValue(saveObj.get("gameRoom"), GameRoom.class);
		
		int result = boardService.gameRoomPassChk(userInfo, gameRoom);
		return result;
	}

	@PostMapping("/rest/dupliChk")
	public int dupliChk(@RequestBody UserInfo userInfo) {
		int result = userService.dupliChk(userInfo);
		
		return result;
	}
}
