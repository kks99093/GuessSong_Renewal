package com.guess.song.handller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.guess.song.model.vo.RoomInfo;
import com.guess.song.service.BoardService;
import com.guess.song.util.SocketUtils;
import com.guess.song.util.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
public class SocketHandler extends TextWebSocketHandler{

	@Autowired
	private BoardService boardService;
	@Autowired
	private SocketUtils socketUtils;
	
	//데이터 저장을 하나로 합침
	//RoomList > roomInfo > userList, songList ....	
	public final static HashMap<String, RoomInfo> roomList = new HashMap<>();
	
	
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		super.afterConnectionEstablished(session);
		
		String url = session.getUri().toString(); // js에서 접속한 localhst/chating/방번호 의 주소가 담겨있음
		String roomNumber = parseRoomNumberFromUrl(url); // chating뒤의 방번호만 잘라냄
		String userName = parseUserNameFromUrl(url);
		boolean isNewRoom =  socketUtils.roomNotExist(roomNumber, roomList); // 방 유무 체크
	
		//방 입장시
		if(isNewRoom) {//생성
			socketUtils.createNewRoom(session, roomNumber, userName, roomList);
			
		}else {  //입장
			RoomInfo roomInfo = roomList.get(roomNumber);
			socketUtils.joinExistingRoom(session, roomInfo, userName);
		}

	}
	
	
	
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = Utils.JsonToObjectParser(message.getPayload());
		String roomNumber = (String)jsonObject.get("roomNumber");
		String type = (String)jsonObject.get("type");
		RoomInfo roomInfo = roomList.get(roomNumber);
		JSONObject response = socketUtils.handleMessageType(type, jsonObject, roomInfo, session.getId());
		socketUtils.broadcastMessage(response, roomInfo, session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		socketUtils.removeUserFromRoom(session.getId(), roomList);
		
		super.afterConnectionClosed(session, status);
	}
	
	
	
	
	
	
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ Not Override 메서드 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	
	
	private String parseRoomNumberFromUrl(String url) {
		return (url.split("/chating/")[1]).split("/")[0];
		
	}
	
	private String parseUserNameFromUrl(String url) {
		String userName = (url.split("/chating/")[1]).split("/")[1];
		try {
			userName =  URLDecoder.decode(userName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userName;
	}

	
	

	
}
