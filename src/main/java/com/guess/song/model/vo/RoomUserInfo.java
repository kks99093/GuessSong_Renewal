package com.guess.song.model.vo;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class RoomUserInfo {

	private String color, userName, sessionId;
	private int ready, score;
	private WebSocketSession session;
}
