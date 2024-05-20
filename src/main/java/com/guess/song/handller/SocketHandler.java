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
		String roomNumber = (url.split("/chating/")[1]).split("/")[0]; // chating뒤의 방번호만 잘라냄
		String userName = URLDecoder.decode((url.split("/chating/")[1]).split("/")[1],"UTF-8");
		boolean isNewRoom =  socketUtils.roomNotExist(roomNumber, roomList); // 방 유무 체크
	
		//방 입장시
		if(isNewRoom) {//생성
			socketUtils.createNewRoom(session, roomNumber, userName, roomList);
			
		}else {  //입장
			socketUtils.joinExistingRoom(session, roomNumber, userName, roomList);
		}

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
					List<SongInfo> songList = roomInfo.getSongList();
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
