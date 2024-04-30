package com.guess.song.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.param.SongBoardParam;
import com.guess.song.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardRestController {
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/rest/boardDel")
	public int boardDel(@RequestBody SongBoardParam songBoardParam) {
		int result = boardService.delSongBoard(songBoardParam);
		return result;
		
	}
	
	@PostMapping("/rest/boardPassChk")
	public int boardPassChk(@RequestBody SongBoardParam songBoardParam) {
		int result = boardService.boardPassChk(songBoardParam);
		return result;
		
	}
	
	@PostMapping("rest/songInfoChk")
	public int songInfoChk(@RequestBody GameRoom gameRoom) {
		System.out.println(gameRoom);
		int result = boardService.songInfoChk(gameRoom);		
		return result;
	}

}
