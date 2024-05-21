package com.guess.song.handller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.guess.song.model.entity.SongInfo;
import com.guess.song.model.vo.RoomInfo;
import com.guess.song.model.vo.RoomUserInfo;
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
	
	
	public static void putRoomUserInfo(RoomUserInfo roomUserInfo, String roomNumber) {
		roomList.get(roomNumber).getUserList().put(roomUserInfo.getSessionId(), roomUserInfo);
	}
	
	public static HashMap<String, RoomUserInfo> getUserList(String roomNumber){
		return roomList.get(roomNumber).getUserList();
	}
	
	public static RoomInfo getRoomInfo(String roomNumber) {
		return roomList.get(roomNumber);
	}
	
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
			socketUtils.joinExistingRoom(session, roomNumber, userName, roomList);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		
		String roomNumber = "";
		String mySessionId = session.getId();
		String leftUser = "";
		String color = "";
		HashMap<String, RoomUserInfo> userList = new HashMap<String, RoomUserInfo>();
		//모든 방을 돌며 해당 유저의 sessionId를 지운다(사실상 하나의 방에서만 지움)
		for(String key : roomList.keySet()) {
			userList = getUserList(key);
			
			if(userList.get(mySessionId) != null) {
				leftUser = userList.get(mySessionId).getUserName();				
				color = userList.get(mySessionId).getColor();				
				userList.remove(mySessionId);
				roomNumber = key;
				break;
			}
		}
		
		
		//방에 사람이 0명이면 게임방 삭제
		userList = getUserList(roomNumber);
		if(userList.size() < 1 && !roomNumber.equals("")) {
			RoomInfo roomInfo = getRoomInfo(roomNumber);
			int roomNumberInt = Integer.parseInt(roomNumber);
			if(roomInfo.getCurrentSong() == null) {
				boardService.delGameRoom(roomNumberInt);
			}
		}else {
			//인원수 갱신
			int headCount = userList.size();
						
			//리더 확인후 교체 , DB에 인원수 + 리더 갱신
			String gameReader = getRoomInfo(roomNumber).getReader();								
			String readerId = null;
			if(mySessionId.equals(gameReader)) {
				for(String key : userList.keySet()) {
					gameReader = userList.get(key).getUserName();					
					roomList.get(roomNumber).setReader(key);
					readerId = key;
					break;
				}
				boardService.updHeadCount(roomNumber, headCount, gameReader);
				roomList.get(roomNumber).setReady(1);
			}else {
				gameReader = null;
				boardService.updHeadCount(roomNumber, headCount, gameReader);
			}
			
			
			// 방에 있는 사람들에게 sessionId를 보내 클라이언트 유저목록에서 지움
			for(String key : userList.keySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", "left");
				jsonObject.put("sessionId", mySessionId);
				jsonObject.put("reader", readerId);
				jsonObject.put("leftUser", leftUser);
				jsonObject.put("color", color);
				WebSocketSession wss = userList.get(key).getSession();				
				wss.sendMessage(new TextMessage (jsonObject.toString()));
			}
		}
		


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
