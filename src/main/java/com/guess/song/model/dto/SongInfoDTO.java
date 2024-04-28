package com.guess.song.model.dto;

import java.sql.Timestamp;

import com.guess.song.model.entity.SongBoard;

import lombok.Data;

@Data
public class SongInfoDTO {
	private Integer songPk;
	
	private String youtubeUrl;
	
	private String answer;
	
	private String hint;
	
	private SongBoard songBoard;
	
	private Timestamp createTime;
}
