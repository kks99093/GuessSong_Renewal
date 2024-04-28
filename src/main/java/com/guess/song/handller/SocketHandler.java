package com.guess.song.handller;

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

import com.guess.song.model.dto.SongInfoDTO;
import com.guess.song.model.vo.RoomInfo;
import com.guess.song.model.vo.RoomUserInfo;
import com.guess.song.service.BoardService;
import com.guess.song.util.SocketUtils;
import com.guess.song.util.Utils;

import lombok.Data;

@Component
@Data
public class SocketHandler extends TextWebSocketHandler{

	@Autowired
	private BoardService boardService;
	@Autowired
	private SocketUtils socketUtils = new SocketUtils();
	
	//데이터 저장을 하나로 합침
	//RoomList > roomInfo > userList, songList ....	
	public static HashMap<String, RoomInfo> roomList = new HashMap<String, RoomInfo>();
	
	
	public static void putRoomUserInfo(RoomUserInfo roomUserInfo, String roomNumber) {
		roomList.get(roomNumber).getUserList().put(roomUserInfo.getSessionId(), roomUserInfo);
	}
	
	public static HashMap<String, RoomUserInfo> getUserList(String roomNumber){
		return roomList.get(roomNumber).getUserList();
	}
	
	public static RoomInfo getRoomInfo(String roomNumber) {
		return roomList.get(roomNumber);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		super.afterConnectionEstablished(session);
		
		String url = session.getUri().toString(); // js에서 접속한 localhst/chating/방번호 의 주소가 담겨있음
		String roomNumber = (url.split("/chating/")[1]).split("/")[0]; // chating뒤의 방번호만 잘라냄
		String userName = URLDecoder.decode((url.split("/chating/")[1]).split("/")[2],"UTF-8");
		boolean flag =  socketUtils.roomChk(roomNumber, roomList); // 방 유무 체크
	
		//방 입장시
		if(flag == false) {//생성
			//방 생성시 Map에다가 유저정보를 넣는처리 (songNumber 써서 메서드 하나로 생성 참가 다 처리할 수 있을듯?) 
			int songNumber = Integer.parseInt((url.split("/chating/")[1]).split("/")[1]); //노래리스트 Pk값
			socketUtils.joinRoom(session, roomNumber, songNumber, userName);
			
		}else {  //입장
			//지금 입장한 사람에게 다른사람의 정보를 보냄 (방에 있는 유저의 리스트를 보냄)
			HashMap<String, RoomUserInfo> userList = getUserList(roomNumber); 
			socketUtils.sendUserList(session, userList, roomNumber);
			
			int songNumber = 0; //이걸로 방 생성인지 참가인지 구분
			//지금 입장한 사람의 정보를 해당방의 userList에 추가
			socketUtils.joinRoom(session, roomNumber ,songNumber, userName); 
			socketUtils.sendMyInfo(session, userList, userName);
			//이미 입장해 있는 다른 유저에게 본인 닉네임과 sessionId를 보냄 (메세지를 보낼때는 해당유저의 session을 가져와서 .sendMessage 메서드로 보냄)

		}
		
		
		
		// 내 정보(sessionId)를 클라이언트로 넘겨서 저장함(이후에 보내는 메세지가 누군지 구분하기 위함)
		String color = getUserList(roomNumber).get(session.getId()).getColor();		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("color", color);
		jsonObject.put("type", "sessionId");
		jsonObject.put("sessionId", session.getId());
		jsonObject.put("reader", getRoomInfo(roomNumber).getReader());
		List<SongInfoDTO> songInfoList = getRoomInfo(roomNumber).getSongList();
		int totalSongNum = songInfoList.size();
		jsonObject.put("youtubeUrl", songInfoList.get(0).getYoutubeUrl());
		jsonObject.put("totalSongNum", totalSongNum);
		
		session.sendMessage(new TextMessage(jsonObject.toString()));

	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = Utils.JsonToObjectParser(message.getPayload());
		String roomNumber = (String)jsonObject.get("roomNumber");
		String type = (String)jsonObject.get("type");
		String youtubeUrl = "";
		RoomInfo roomInfo = roomList.get(roomNumber);
		
		//HashMap<String, Object> userList = roomUserInfo.get(roomNumber);
		int readyChk = 0;
		String beforeAnswer = "";
		
		switch(type) {
			case "gameStart" :
				readyChk = readyChk(roomNumber);
				if(readyChk == 1) { 
					roomInfo.setCurrentSong(0);
				}
				jsonObject.put("readyChk", readyChk);
				break;
			case "skipSong" :
				beforeAnswer = socketUtils.getBeforeAnswer(roomInfo);
				int skipChk = socketUtils.skipSong(roomInfo, roomNumber);
				jsonObject.put("skipChk", skipChk);
				if(skipChk == 1) {
					youtubeUrl = socketUtils.nextYoutubeUrl(roomInfo);
					jsonObject.put("youtubeUrl", youtubeUrl);
					jsonObject.put("beforeAnswer", beforeAnswer);
				}else if(skipChk == -1){
					jsonObject.put("skipChk", skipChk);
				}else {
					int skipCount = roomInfo.getSkipCount();
					jsonObject.put("skipCount", skipCount);
				}
				
				break;
			case "ready":
				userReady(roomNumber, 1, session.getId());
				jsonObject.put("sessionId", session.getId());
				break;
			case "message" :
				String answerReady = (String)jsonObject.get("answerReady");
				if(roomInfo.getCurrentSong() != null && answerReady.equals("1")) {
					String userMsg = ((String)jsonObject.get("msg")).replaceAll("\\s", "");
					List<SongInfoDTO> songList = roomInfo.getSongList();
					int currentSong = roomInfo.getCurrentSong();
					beforeAnswer = songList.get(currentSong).getAnswer();
					int answerChk = socketUtils.answerChk(songList, currentSong, userMsg, session.getId(), roomInfo.getUserList());
					if(answerChk == 1) {
						int score = roomInfo.getUserList().get(session.getId()).getScore();
						jsonObject.put("beforeAnswer", beforeAnswer);
						jsonObject.put("score", score);
					}
					String nextYoutubeUrl = socketUtils.nextYoutubeUrl(roomInfo);
					jsonObject.put("youtubeUrl", nextYoutubeUrl);
					jsonObject.put("answerChk", answerChk);
					
				}
				String sessionId = (String) jsonObject.get("sessionId");
				String userName = roomInfo.getUserList().get(sessionId).getUserName();								
				String userColor = roomInfo.getUserList().get(sessionId).getColor();
				jsonObject.put("color", userColor);
				jsonObject.put("sessionId", session.getId());
				jsonObject.put("userName", userName);
				break;
			case "readyCencel" :
				userReady(roomNumber, -1, session.getId());
				jsonObject.put("sessionId", session.getId());
				break;
			case "nextSongChk" :
				int nextSongChk = nextSongChk(roomNumber);
				int currentSong = roomInfo.getCurrentSong();
				jsonObject.put("currentSong", currentSong);
				jsonObject.put("nextSongChk", nextSongChk);
				break;
			case "resultSong" :
				int resultChk = socketUtils.resultSong(roomInfo, roomNumber);
				if(jsonObject.get("answerToEnd") != null &&(Boolean)jsonObject.get("answerToEnd")) {
					resultChk = 1;
				}
				jsonObject.put("resultChk", resultChk);
				if(resultChk == 0) {
					int resultCount = roomInfo.getResultCount();					
					jsonObject.put("resultCount", resultCount);
				}else {
					beforeAnswer = socketUtils.getBeforeAnswer(roomInfo);
					List<HashMap<String, String>> endUserList = socketUtils.endGameUserList(roomInfo.getUserList(), roomNumber);					
					jsonObject.put("userList", endUserList);
					jsonObject.put("beforeAnswer", beforeAnswer);
				}
				break;
			
		}
		
		//HashMap<String, HashMap<String, Object>> userList = (HashMap<String, HashMap<String, Object>>) roomList.get(roomNumber).get("userList");
		if(type.equals("gameStart") && readyChk == 0) {
			session.sendMessage(new TextMessage(jsonObject.toString()));
		}else {
			//userList를 돌며 session을 가져와서 메세지를 보냄
			for(String key : roomInfo.getUserList().keySet()) {
				WebSocketSession wss = roomInfo.getUserList().get(key).getSession(); 
				
				try {
					synchronized(wss) {
						wss.sendMessage(new TextMessage(jsonObject.toString()));
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		

		super.handleTextMessage(session, message);
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
			if(roomInfo.getCurrentSong() == null) {
				boardService.delGameRoom(roomNumber);
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
	
	
	

	
	public void userReady(String roomNumber, int count, String sessionId) {
		int readyHead = roomList.get(roomNumber).getReady() + count;
		roomList.get(roomNumber).setReady(readyHead);

		if(count == -1) {	
			roomList.get(roomNumber).getUserList().get(sessionId).setReady(0);
		}else {
			roomList.get(roomNumber).getUserList().get(sessionId).setReady(1);
		}

	}
	
	public int readyChk (String roomNumber) {
		int readyChk = 0;
		int readyHead = roomList.get(roomNumber).getReady();		
		int headCount = roomList.get(roomNumber).getUserList().size();		
		if(readyHead == headCount) {
			readyChk = 1;
			//게임이 시작됐으면 게임목록에서 이 게임방 삭제
			boardService.delGameRoom(roomNumber);
		}
		return readyChk;
	}
	
	
	

	

	
	public int nextSongChk(String roomNumber) {
		int nextSongChk = 0;
		RoomInfo roomInfo = getRoomInfo(roomNumber);
			
		roomInfo.setNextSongChk(roomInfo.getNextSongChk()+1);
		
		if(roomInfo.getNextSongChk() == roomInfo.getUserList().size()) {
			int currentSong = roomInfo.getCurrentSong();
			roomInfo.setCurrentSong(currentSong+1);
			nextSongChk = 1;
			roomInfo.setNextSongChk(0);
		}
		return nextSongChk;
	}
	
	

	
	

	
}
