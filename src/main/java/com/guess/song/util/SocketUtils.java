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
		int roomNumberInt = Integer.parseInt(roomNumber);
		newRoom.setRoomPk(roomNumberInt);
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
	
	//닉네임 컬러
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
	
	
	
	//받은 메세지를 분류후에 처리하는 부분
	public JSONObject handleMessageType(String type, JSONObject jsonObject, RoomInfo roomInfo, String sessionId) {
		switch(type) {
			case "gameStart" :
				return handleGameStart(jsonObject, roomInfo);
			case "skipSong" :
				return handleSkipSong(jsonObject, roomInfo);
			case "ready":
				return handleReady(jsonObject, roomInfo, sessionId);
			case "readyCancel" :
				return handleReadyCancel(jsonObject, roomInfo, sessionId);
			case "message" :
				return handleMessage(jsonObject, roomInfo, sessionId);	
			case "nextSongChk" :
				return handleNextSongChk(jsonObject, roomInfo);
			case "resultSong" :
				return handleResulSong(jsonObject, roomInfo);
		
		}
		return jsonObject;
		
	}
	

	//게임 시작
	@SuppressWarnings("unchecked")
	public JSONObject handleGameStart(JSONObject jsonObject, RoomInfo roomInfo) {
		int readyChk = readyChk(roomInfo);
		jsonObject.put("readyChk", readyChk);
		if(readyChk == 1) {
			//게임이 시작된 경우 게임 목록에서 방을 삭제하고 현재 노래목록에 첫번째 노래를 넣음
			boardService.delGameRoom(roomInfo.getRoomPk());
			roomInfo.setCurrentSong(0);
		}
		
		return jsonObject;
		
	}
	
	//모두 준비 완료 했는지 확인
	public int readyChk (RoomInfo roomInfo) {
		int readyChk = 0;
		int readyHead = roomInfo.getReady();
		int headCount = roomInfo.getUserList().size();
		if(readyHead == headCount) {
			readyChk = 1;			
		}
		return readyChk;
	}
	
	
	
	//게임 스킵
	@SuppressWarnings("unchecked")
	public JSONObject handleSkipSong(JSONObject jsonObject, RoomInfo roomInfo) {		
		int skipChk = skipSong(roomInfo);
		jsonObject.put("skipChk", skipChk);
		if(skipChk == 1) {
			String youtubeUrl = nextYoutubeUrl(roomInfo);
			String beforeAnswer = getBeforeAnswer(roomInfo);
			jsonObject.put("youtubeUrl", youtubeUrl);
			jsonObject.put("beforeAnswer", beforeAnswer);
		}else if(skipChk == -1){
			jsonObject.put("skipChk", skipChk);
		}else {
			int skipCount = roomInfo.getSkipCount();
			jsonObject.put("skipCount", skipCount);
		}
		
		return jsonObject;
	}
	
	
	//스킵했을경우 정답을 알려주기 위한 메서드
	public String getBeforeAnswer(RoomInfo roomInfo) {
		List<SongInfo> songList = roomInfo.getSongList();
		int currentSong = roomInfo.getCurrentSong();
		String beforeanswer = songList.get(currentSong).getAnswer();
		return beforeanswer;
	}
		
	//노래를 스킵할지 인원수 확인
	public int skipSong(RoomInfo roomInfo) {
		int skipChk = 0;
		
		if("".equals(nextYoutubeUrl(roomInfo))) {
			skipChk = -1; //마지막 곡이었음 (skipChk = -1)
		}else {					
			if(roomInfo.getSkipCount() == null) {
				//제일 처음 스킵을 누른경우 skipCount의 초기값 설정
				roomInfo.setSkipCount(1);
			}else {
				//두번째 부터 스킵을 누른경우 skipCount의 값에 + 1
				int skipCount = roomInfo.getSkipCount();
				roomInfo.setSkipCount(skipCount + 1);
			}
			int skipCount = roomInfo.getSkipCount();
			// skipCount가 과반수 이상일 경우 노래 스킵 (skipChk = 1)
			if(skipCount > (roomInfo.getUserList().size()/2)) {				
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
	

	
	// 준비 버튼
	@SuppressWarnings("unchecked")
	public JSONObject handleReady(JSONObject jsonObject, RoomInfo roomInfo, String sessionId) {
		userReady(roomInfo, 1, sessionId);
		jsonObject.put("sessionId", sessionId);
		return jsonObject;
	}
	
	//준비 취소 버튼
	@SuppressWarnings("unchecked")
	public JSONObject handleReadyCancel(JSONObject jsonObject, RoomInfo roomInfo, String sessionId) {
		userReady(roomInfo, -1, sessionId);
		jsonObject.put("sessionId", sessionId);
		return jsonObject;
	}
	
	public void userReady(RoomInfo roomInfo, int count, String sessionId) {
		int readyHead = roomInfo.getReady() + count; 
		roomInfo.setReady(readyHead);

		if(count == -1) {
			roomInfo.getUserList().get(sessionId).setReady(0);
		}else {
			roomInfo.getUserList().get(sessionId).setReady(1);
		}

	}
	
	
	//메세지
	@SuppressWarnings("unchecked")
	public JSONObject handleMessage(JSONObject jsonObject, RoomInfo roomInfo, String sessionId) {
		String answerReady = (String) jsonObject.get("answerReady");//노래 시작전에 막 적은 채팅이 정답되는걸 방지하기 위해 추가
		if(roomInfo.getCurrentSong() != null && answerReady.equals("1")) {
			String userMsg = (String)jsonObject.get("msg");
			List<SongInfo> songList = roomInfo.getSongList();
			int currentSong = roomInfo.getCurrentSong();
			String beforeAnswer = songList.get(currentSong).getAnswer(); // answerChk에서 정답일 경우 null로 바뀔거라 미리 받아놓음			
			int answerChk = answerChk(songList, roomInfo, userMsg, sessionId);
			if(answerChk == 1) {
				int score = roomInfo.getUserList().get(sessionId).getScore();
				jsonObject.put("beforeAnswer", beforeAnswer);
				jsonObject.put("score", score);
			}
			String nextYoutubeUrl = nextYoutubeUrl(roomInfo);
			jsonObject.put("youtubeUrl", nextYoutubeUrl);
			jsonObject.put("answerChk", answerChk);
		}
		String userName = roomInfo.getUserList().get(sessionId).getUserName();								
		String userColor = roomInfo.getUserList().get(sessionId).getColor();
		jsonObject.put("color", userColor);
		jsonObject.put("sessionId", sessionId);
		jsonObject.put("userName", userName);
		return jsonObject;
	}
	

	//정답 확인
	public int answerChk(List<SongInfo> songList, RoomInfo roomInfo, String userMsg, String sessionId) {
		int answerChk = 0;
		int currentSong = roomInfo.getCurrentSong();
		if(songList.get(currentSong).getAnswer() != null) {
			String answer = songList.get(currentSong).getAnswer().replaceAll("\\s", "").toLowerCase(); // 정답 공백 및 소문자로 전환
			userMsg = userMsg.replaceAll("\\s", "").toLowerCase();// 공백제거 및 소문자 전환 msg
			if(answer.equals(userMsg)) {				
				answerChk = 1; //정답일경우 1, 오답일경우 0
				songList.get(currentSong).setAnswer(null); // 중복 정답을 없애기위해 정답칸을 null로 변경
				int score = roomInfo.getUserList().get(sessionId).getScore();
				roomInfo.getUserList().get(sessionId).setScore(score+1);				
			}
						
		}		
		return answerChk;	
	}
	
	
	//다음 노래 셋팅
	@SuppressWarnings("unchecked")
	public JSONObject handleNextSongChk(JSONObject jsonObject, RoomInfo roomInfo) {
		int nextSongChk = nextSongChk(roomInfo);
		int currentSong = roomInfo.getCurrentSong();
		jsonObject.put("currentSong", currentSong);
		jsonObject.put("nextSongChk", nextSongChk);
		return jsonObject;
	}
	
	//모든 유저의 클라이언트에 다음 노래를 재생할 준비가 되었는지 확인
	public int nextSongChk(RoomInfo roomInfo) {
		int nextSongChk = 0;
		roomInfo.setNextSongChk(roomInfo.getNextSongChk()+1);		
		if(roomInfo.getNextSongChk() == roomInfo.getUserList().size()) {
			int currentSong = roomInfo.getCurrentSong();
			roomInfo.setCurrentSong(currentSong+1);
			nextSongChk = 1;
			roomInfo.setNextSongChk(0);
		}
		return nextSongChk;
	}
	
	
	
	//결과창 보기 (resultChk가 1일경우 결과창 보기로 넘어감)
	@SuppressWarnings("unchecked")
	public JSONObject handleResulSong(JSONObject jsonObject, RoomInfo roomInfo) {
		int resultChk = resultSong(roomInfo);
		//answerToEnd => 다음 노래가 없어서 게임을 끝낼때 클라이언트에서 보냄 (혹시 다음 노래가 없는데 게임이 끝나지 않을경우가 있을까봐 넣어놨음)
		if(jsonObject.get("answerToEnd") != null &&(Boolean)jsonObject.get("answerToEnd")) {
			resultChk = 1;
		}
		jsonObject.put("resultChk", resultChk);
		if(resultChk == 0) {
			int resultCount = roomInfo.getResultCount();
			jsonObject.put("resultCount", resultCount);
		}else {
			String beforeAnswer = getBeforeAnswer(roomInfo);
			List<HashMap<String, String>> endUserList = endGameUserList(roomInfo);
			jsonObject.put("userList", endUserList);
			jsonObject.put("beforeAnswer", beforeAnswer);
		}
		
		
		return jsonObject;				
	}
	
		
	
	//결과창보기 누른 인원수 확인 (skipSong이랑 비슷)
	public int resultSong(RoomInfo roomInfo) {
		int resultChk = 0;
		if(roomInfo.getResultCount() == null) {
			//resultCount 초기값
			roomInfo.setResultCount(1);
		}else {
			int resultCount = roomInfo.getResultCount();
			roomInfo.setResultCount(resultCount + 1);
		}
		int resultCount = roomInfo.getResultCount();
		//과반수 이상이 동의 했다면 게임 끝내기
		if(resultCount>(roomInfo.getUserList().size()/2)) {			
			resultChk = 1;
		}
		return resultChk;		
	}
	
	
	//결과창에서 보여줄 유저리스트 닉네임(컬러포함) , 스코어만 가져와서 LIST로 만들어서 return
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> endGameUserList(RoomInfo roomInfo){
		HashMap<String, RoomUserInfo> userListParam = roomInfo.getUserList();
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
				e.printStackTrace();
			}
			
		}
		return userList;
	}
	
	
	public void broadcastMessage(JSONObject jsonObject, RoomInfo roomInfo, WebSocketSession session) {
		if(jsonObject.get("type").equals("gameStart") && (int)jsonObject.get("readyChk") != 1) {
			try {
				session.sendMessage(new TextMessage(jsonObject.toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			for(String key : roomInfo.getUserList().keySet()) {
				WebSocketSession wss = roomInfo.getUserList().get(key).getSession();
				try {
					wss.sendMessage(new TextMessage(jsonObject.toString()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	
}
