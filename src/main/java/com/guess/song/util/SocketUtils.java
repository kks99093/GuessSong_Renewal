package com.guess.song.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guess.song.handller.SocketHandler;
import com.guess.song.model.entity.SongInfo;
import com.guess.song.model.vo.RoomInfo;
import com.guess.song.model.vo.RoomUserInfo;
import com.guess.song.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketUtils {
	
	@Autowired
	private BoardService boardService;
	
	private ObjectMapper mapper = new ObjectMapper();
	public boolean roomNotExist(String roomNumber, HashMap<String, RoomInfo> roomList) {
		//roomList에 해당 roomNuber가 존재하는지 확인
		return !roomList.containsKey(roomNumber);
	}
	
	//방 생성 메서드
	public void createNewRoom(WebSocketSession session, String roomNumber, String userName, Map<String, RoomInfo> roomList) {
		RoomInfo newRoom = new RoomInfo();
		roomList.put(roomNumber, newRoom);
		HashMap<String, RoomUserInfo> userList = new HashMap<>();
		newRoom.setUserList(userList);
		joinRoom(session, true, roomNumber, userName, roomList);
	}
	
	//방 입장 메서드	
	public void joinExistingRoom(WebSocketSession session, String roomNumber, String userName, Map<String, RoomInfo> roomList) {
		sendUserList(session, roomList.get(roomNumber).getUserList(), roomNumber); //방에 접속한 유저들의 정보를 나한테 보냄
		joinRoom(session, false, roomNumber, userName, roomList); //나의 정보를 서버에 입력
		sendMyInfo(session, roomList.get(roomNumber).getUserList(), userName); // 내 정보를 방에 접속한 유저들에게 보냄
	}
	
	
	//방 입장시 공통적인 부분 처리
	public void joinRoom(WebSocketSession session, boolean isNewRoom, String roomNumber, String userName, Map<String, RoomInfo> roomList) {
		RoomInfo roomInfo = roomList.get(roomNumber);
		RoomUserInfo roomUserInfo = new RoomUserInfo(session.getId(), userName, session);		
		if(isNewRoom) {
			boardService.getRoomInfo(roomNumber, roomInfo);
			roomInfo.setReader(session.getId());
			roomInfo.setNextSongChk(0);
			roomUserInfo.setColor("red");
		}else {
			String color = searchingColor(roomInfo.getUserList());
			roomUserInfo.setColor(color);
		}
		roomInfo.getUserList().put(session.getId(), roomUserInfo);		
		sendInitMessages(session, roomInfo);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void sendInitMessages(WebSocketSession session, RoomInfo roomInfo) {		
        try {
        	String color = roomInfo.getUserList().get(session.getId()).getColor();
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("color", color);
    		jsonObject.put("type", "sessionId");
    		jsonObject.put("sessionId", session.getId());
    		jsonObject.put("reader", roomInfo.getReader());
    		List<SongInfo> songInfoList = roomInfo.getSongList();
    		int totalSongNum = songInfoList.size();
            jsonObject.put("youtubeUrl", songInfoList.get(0).getYoutubeUrl());
            jsonObject.put("totalSongNum", totalSongNum);
			session.sendMessage(new TextMessage(jsonObject.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public String searchingColor(HashMap<String, RoomUserInfo> userList) {
		String[] colorList = {"red", "blue", "green", "gray", "black", "brown", "purple", "yellow"};
		String color = "";
		for(String key : userList.keySet()) {		
			color = userList.get(key).getColor();
			
			for(int i = 0; i < colorList.length; i++) {
				if(color.equals(colorList[i])) {
					List<String> result = new ArrayList<>(Arrays.asList(colorList));
					result.remove(i);
					colorList = result.toArray(new String[0]);
					break;
				}
			}
		}
		
		color = colorList[0];
		return color; 
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void sendUserList(WebSocketSession session, HashMap<String, RoomUserInfo> userListParam, String roomNumber) {
		List<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
		
		for(String key : userListParam.keySet()) {
			RoomUserInfo roomUserInfo = new RoomUserInfo();
			String userName = userListParam.get(key).getUserName();
			String userColor = userListParam.get(key).getColor(); 			
			int ready = userListParam.get(key).getReady();
			roomUserInfo.setUserName(userName);
			roomUserInfo.setColor(userColor);
			roomUserInfo.setReady(ready);
			roomUserInfo.setSessionId(key);
			
			try {
				HashMap<String, String> userInfoMap = (HashMap<String, String>) mapper.convertValue(roomUserInfo, Map.class);
				userList.add(userInfoMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		String reader = SocketHandler.getRoomInfo(roomNumber).getReader();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "join");
		jsonObject.put("reader", reader);
		jsonObject.put("userList", userList);
		try {
			session.sendMessage(new TextMessage(jsonObject.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	// 내 정보를 방의 사람들에게 보냄
	@SuppressWarnings("unchecked")
	public void sendMyInfo(WebSocketSession session, HashMap<String, RoomUserInfo> userList, String userName) {		
		
		RoomUserInfo roomUserInfo = new RoomUserInfo();
		String userColor = userList.get(session.getId()).getColor();
		roomUserInfo.setColor(userColor);
		roomUserInfo.setSessionId(session.getId());
		roomUserInfo.setUserName(userName);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "join");
		try {
			HashMap<String, String> mapUserInfo = (HashMap<String, String>) mapper.convertValue(roomUserInfo, Map.class);
			jsonObject.put("user", mapUserInfo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		jsonObject.put("color", userColor);
		for(String key : userList.keySet()) {
			if(key.equals(session.getId())) {
				continue;
			}
			WebSocketSession wss = userList.get(key).getSession();;
											
			try {
				wss.sendMessage(new TextMessage(jsonObject.toString()));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public String getBeforeAnswer(RoomInfo roomInfo) {
		List<SongInfo> songList = roomInfo.getSongList();
		int currentSong = roomInfo.getCurrentSong();
		String beforeanswer = songList.get(currentSong).getAnswer();
		return beforeanswer;
	}
	
	
	
	public int skipSong(RoomInfo roomInfo, String roomNumber) {
		int skipChk = 0;
		
		if("".equals(nextYoutubeUrl(roomInfo))) {
			skipChk = -1; //마지막 곡이었음
		}else {
			HashMap<String, RoomUserInfo> userList = roomInfo.getUserList();			
			if(roomInfo.getSkipCount() == null) {
				SocketHandler.roomList.get(roomNumber).setSkipCount(1);
			}else {
				int skipCount = roomInfo.getSkipCount();
				SocketHandler.roomList.get(roomNumber).setSkipCount(skipCount + 1);
			}
			int skipCount = roomInfo.getSkipCount();
			if(skipCount>(userList.size()/2)) {
				roomInfo.setSkipCount(0);
				skipChk = 1;
			}
			
		}
		return skipChk;
	}
	

	
	public String nextYoutubeUrl(RoomInfo roomInfo) {
		String youtubeUrl = "";
		int currentSong = roomInfo.getCurrentSong();
		List<SongInfo> songList = roomInfo.getSongList();
		if(songList.size() > (currentSong+1)) {
			youtubeUrl = songList.get(currentSong+1).getYoutubeUrl();
		}
		return youtubeUrl;
		
	}
	
	
	public int resultSong(RoomInfo roomInfo, String roomNumber) {
		int resultChk = 0;
		HashMap<String, RoomUserInfo> userList = roomInfo.getUserList();
		if(roomInfo.getResultCount() == null) {
			SocketHandler.roomList.get(roomNumber).setResultCount(1);
		}else {
			int resultCount = roomInfo.getResultCount();
			SocketHandler.roomList.get(roomNumber).setResultCount(resultCount + 1);
		}
		int resultCount = roomInfo.getResultCount();		
		if(resultCount>(userList.size()/2)) {
			resultChk = 1;
		}
		return resultChk;
		
		
	}
	
	
	//게임 시작한 후 보낸 메세지가 정답인지 확인하는 로직
	public int answerChk(List<SongInfo> songList, int currentSong, String userMsg, String sessionId, HashMap<String, RoomUserInfo> userList) {
		int answerChk = 0;
		if(songList.get(currentSong).getAnswer() != null) { //정답칸이 비어있다 => 이미 정답자가 나왔다는 뜻이므로 정답체크 할 필요가 없음
			String answer = songList.get(currentSong).getAnswer().replaceAll("\\s", ""); //정답
			answer = answer.toLowerCase();
			userMsg = userMsg.replaceAll("\\s", ""); //공백 제거한 msg
			userMsg = userMsg.toLowerCase();
			if(answer.equals(userMsg)) {
				answerChk = 1; //정답일경우 answerChk에 1을 넣어서 리턴, 오답일경우 0임					
				songList.get(currentSong).setAnswer(null); //정답자가 중복해서 나오지않게 정답칸을 바로 비워줌
				int score = userList.get(sessionId).getScore();				
				score = score + 1;
				userList.get(sessionId).setScore(score);
				
			}
		}
		return answerChk;
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> endGameUserList(HashMap<String, RoomUserInfo> userListParam, String roomNumber){
		List<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();
		
		for(String key : userListParam.keySet()) {
			RoomUserInfo roomUserInfo = new RoomUserInfo();
			String userName = userListParam.get(key).getUserName();			
			String color = userListParam.get(key).getColor();
			int score = userListParam.get(key).getScore();
			roomUserInfo.setColor(color);
			roomUserInfo.setScore(score);
			roomUserInfo.setUserName(userName);
			try {
								
				HashMap<String, String> mapUserInfo = (HashMap<String, String>) mapper.convertValue(roomUserInfo, Map.class);
				userList.add(mapUserInfo);
			} catch (Exception e) {
				// TODO: handle exception
			}

			
			
			
		}
		return userList;
	}
}
