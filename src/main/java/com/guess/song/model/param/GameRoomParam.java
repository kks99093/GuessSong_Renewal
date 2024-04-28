package com.guess.song.model.param;

import java.sql.Timestamp;

import javax.persistence.OneToOne;

import com.guess.song.model.entity.SongBoard;

import lombok.Data;

@Data
public class GameRoomParam {
	private Integer roomPk;
	
	private String title;
	
	private String password;
	
	private String reader;
	
	private int amount;

	private String userName;
	
	private int createRoom;
	
	@OneToOne
	private SongBoard songBoard;
	
	private Timestamp createTime;
}
