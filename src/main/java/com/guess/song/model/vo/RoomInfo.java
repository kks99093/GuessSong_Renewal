package com.guess.song.model.vo;

import java.util.HashMap;
import java.util.List;

import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.SongInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomInfo {
	private Integer roomPk;
	private String reader;
	private Integer currentSong, amount, headCount, ready, skipCount, resultCount, nextSongChk;
	private List<SongInfo> songList;
	private HashMap<String, RoomUserInfo> userList;
	
	public RoomInfo(GameRoom gameRoom) {
		this.roomPk = gameRoom.getRoomPk();
		this.reader = gameRoom.getReader();
		this.amount = gameRoom.getAmount();
		this.headCount = gameRoom.getHeadCount();
		
	}

}
