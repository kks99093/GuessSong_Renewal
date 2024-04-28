package com.guess.song.controller.rest;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guess.song.model.param.GameRoomParam;
import com.guess.song.service.BoardService;

@RestController
public class UserRestController {
	
	@Autowired
	private BoardService boardService;
	
	
	@PostMapping("/rest/userNameChk")
	public int userNameChk(@RequestBody GameRoomParam gameRoomParam) throws UnsupportedEncodingException {
		int result = boardService.gameRoomPassChk(gameRoomParam);
		return result;
	}

}
