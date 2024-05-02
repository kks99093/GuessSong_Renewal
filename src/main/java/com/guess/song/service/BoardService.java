package com.guess.song.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.guess.song.handller.SocketHandler;
import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.SongBoard;
import com.guess.song.model.entity.SongInfo;
import com.guess.song.model.entity.UserInfo;
import com.guess.song.model.param.SongInfoParam;
import com.guess.song.model.vo.RoomInfo;
import com.guess.song.model.vo.RoomUserInfo;
import com.guess.song.repository.GameRoomRepository;
import com.guess.song.repository.SongBoardRepository;
import com.guess.song.repository.SongInfoRepository;
import com.guess.song.util.Utils;

@Service
public class BoardService {
	
	@Autowired
	private SongInfoRepository songRep;
	
	@Autowired
	private SongBoardRepository songBoardRep;
	
	@Autowired
	private GameRoomRepository gameRoomRep;
	

	
	//노래 정보 DB에 등록
	public void insSong(SongInfoParam songInfoParam) {
		for(int i = 0; i < songInfoParam.getAnswer().size(); i++) {
			SongInfo songInfo = new SongInfo();
			//유튜브 url 확인
			String youtubeUrl =songInfoParam.getYoutubeUrl().get(i);
			if(youtubeUrl.contains("youtu.be")) {
				int idx = youtubeUrl.lastIndexOf("/");
				youtubeUrl = youtubeUrl.substring(idx+1);
				
			}else if(youtubeUrl.contains("youtube.com")) {
				int startIdx = youtubeUrl.indexOf("v=")+2;
				int endIdx = youtubeUrl.indexOf("&");
				youtubeUrl = youtubeUrl.substring(startIdx, endIdx);
			}else {
				continue;
			}
			
			if(songInfoParam.getYear().get(i) > 2024 || songInfoParam.getYear().get(i) < 1990) {
				continue;
			}
			songInfo.setYear(songInfoParam.getYear().get(i));			
			songInfo.setYoutubeUrl(youtubeUrl);
			String answer = Utils.htmlTagChg(songInfoParam.getAnswer().get(i));
			songInfo.setAnswer(answer);
			String hint = Utils.htmlTagChg(songInfoParam.getHint().get(i));
			songInfo.setHint(hint);
			songInfo.setCategory(songInfoParam.getCategory().get(i));
	
			songRep.save(songInfo);
		}
		
		

	}

	
	//방목록 불러오기
	public Page<SongBoard> selSongBoardList(Pageable pageable, String searchText){
		if(searchText == null || searchText.equals("")) {
			Page<SongBoard> songBoardList = songBoardRep.findAll(pageable);
			return songBoardList;
		}else {
			searchText = "%"+ searchText +"%";
			Page<SongBoard> songBoardList = songBoardRep.findByTitleLike(pageable, searchText);
			return songBoardList;
		}
				
		
	}

	
	public List<SongInfo> findSongList(GameRoom gameRoom){
		List<SongInfo> songList = new ArrayList<SongInfo>();		
		if(gameRoom.getCategory().equals("all")) {
			songList = songRep.findSongList(gameRoom.getBeforeYears(), gameRoom.getAfterYears(), gameRoom.getCount());
		}else {
			songList = songRep.findSongList(gameRoom.getCategory(), gameRoom.getBeforeYears(), gameRoom.getAfterYears(), gameRoom.getCount());
		}
		return songList;
	}
	
	
	
	public GameRoom insGameRoom(GameRoom gameRoom, UserInfo userInfo) {
		GameRoom result = new GameRoom();
		
		if(gameRoom.getRoomPk() == null){
			String title = Utils.htmlTagChg(gameRoom.getTitle());
			gameRoom.setTitle(title);
			String reader = Utils.htmlTagChg(userInfo.getName());
			gameRoom.setReader(reader);
			gameRoom.setAmount(gameRoom.getAmount());
			gameRoom.setHeadCount(1);
			if(gameRoom.getPassword() != null && !gameRoom.getPassword().equals("")) {
				String salt = Utils.getSalt();
				String cryptPw = Utils.getBcryptPw(salt, gameRoom.getPassword());
				gameRoom.setSalt(salt);
				gameRoom.setPassword(cryptPw);
			}else {
				gameRoom.setPassword(null);
				gameRoom.setSalt(null);
			}
			gameRoom = gameRoomRep.save(gameRoom);
			result = gameRoom;
		}else {
			result = gameRoomRep.findByRoomPk(gameRoom.getRoomPk());
		}
		
		return result;

	}
	
	
	public Page<GameRoom> selGameRoom(Pageable pageable){
		Page<GameRoom> gameRoomList = gameRoomRep.findAll(pageable);
		return gameRoomList;
	}
	
	public void delGameRoom(String roomNumberParam) {
		int roomNumber = Integer.parseInt(roomNumberParam);
		GameRoom gameRoom = gameRoomRep.findByRoomPk(roomNumber);
		gameRoomRep.delete(gameRoom);
	}
	
	
	public int delSong(int songBoardPk) {
		int result = 0;
		List<SongInfo> songInfoList = songRep.findByBoardPk(songBoardPk);
		if(songInfoList != null) {
			try {
				for(SongInfo songInfo : songInfoList) {
					songRep.delete(songInfo);
				}
				result = 1;
			}catch(Exception e) {
				e.printStackTrace();
				result = 0;
			}
		}else {
			result =1;
		}
		
		return result;
	}
	

	//게임방 비밀번호, 인원, 중복아이디 체크
	public int gameRoomPassChk(UserInfo userInfo, GameRoom gameRoomParam) {
		int result = 1;
		GameRoom gameRoom = gameRoomRep.findByRoomPk(gameRoomParam.getRoomPk());
		if(gameRoom.getAmount() - gameRoom.getHeadCount() == 0) {
			return  -2; //인원이 가득참
		}
		
		if(gameRoomParam.getPassword() != null) {
			String crypPw = Utils.getBcryptPw(gameRoom.getSalt(), gameRoomParam.getPassword());
			if(!crypPw.equals(gameRoom.getPassword())) {
				return result = -1;//비밀번호가 틀림
			}
		}
		
		
		String roomNumber = gameRoomParam.getRoomPk()+"";
		String userNameParam = userInfo.getName();
		HashMap<String, RoomUserInfo> userList = SocketHandler.getUserList(roomNumber);
		for(String key : userList.keySet()) {
			String userName = userList.get(key).getUserName();			
			if(userNameParam.equals(userName)) {
				result = 0; // 중복된 아이디가 있음
				break;
			}
		}
		
		return result;
	}
	
	public RoomInfo getRoomInfo(String roomNuberStr) {
		int roomNumber = Integer.parseInt(roomNuberStr);
		GameRoom gameRoom = gameRoomRep.findByRoomPk(roomNumber);
		List<SongInfo> songList = findSongList(gameRoom);
		RoomInfo roomInfo = new RoomInfo(gameRoom);
		roomInfo.setReady(1);
		roomInfo.setSongList(songList);		
		
		return roomInfo;
	}
	
	public void updHeadCount(String roomNumberStr, int headCount, String gameReader) {
		int roomNumber = Integer.parseInt(roomNumberStr);
		GameRoom gameRoom = gameRoomRep.findByRoomPk(roomNumber);
		if(gameRoom != null && gameReader != null) {
			gameRoom.setReader(gameReader);
		}
		if(gameRoom != null) {
			gameRoom.setHeadCount(headCount);
			gameRoomRep.save(gameRoom);
		}
		
	}
	
	
	public int songInfoChk(GameRoom gameRoom) {
		int result = 0;
		if(gameRoom.getCategory().equals("all")) {
			result = songRep.songInfoChk(gameRoom.getBeforeYears(), gameRoom.getAfterYears());
		}else {
			result = songRep.songInfoChk(gameRoom.getCategory(), gameRoom.getBeforeYears(), gameRoom.getAfterYears());
		}
		 
		if(result > gameRoom.getCount()) {
			return 1;
		}else {
			return -1;
		}
		
	}

}
