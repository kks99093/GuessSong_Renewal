package com.guess.song.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.SongInfo;
import com.guess.song.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardRestController {
	
	@Autowired
	private BoardService boardService;
	
	
	@PostMapping("rest/songInfoChk")
	public int songInfoChk(@RequestBody GameRoom gameRoom) {
		System.out.println(gameRoom);
		int result = boardService.songInfoChk(gameRoom);		
		return result;
	}
	
	@PostMapping("rest/updSong")
	public String updSong(@RequestBody SongInfo songInfo) {
		String result = boardService.updSong(songInfo);
		return result;
	}
	
	@PostMapping("rest/delSong")
	public int delSong(@RequestBody SongInfo songInfo) {		
		int result = boardService.delSong(songInfo);
		
		return result;
	}

}
