package com.guess.song.model.vo;

import java.util.HashMap;
import java.util.List;

import com.guess.song.model.dto.SongInfoDTO;
import com.guess.song.model.entity.GameRoom;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomInfo {
	private Integer roomPk;
	private String reader;
	private Integer currentSong, amount, headCount, ready, skipCount, resultCount, nextSongChk;
	private List<SongInfoDTO> songList;
	private HashMap<String, RoomUserInfo> userList;
	
	public RoomInfo(GameRoom gameRoom) {
		this.roomPk = gameRoom.getRoomPk();
		this.reader = gameRoom.getReader();
		this.amount = gameRoom.getAmount();
		this.headCount = gameRoom.getHeadCount();
		
	}

}
