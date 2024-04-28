package com.guess.song.model.param;

import java.sql.Timestamp;
import java.util.List;

import com.guess.song.model.entity.SongInfo;

import lombok.Data;

@Data
public class SongBoardParam {
	
	private Integer boardPk;
	
	private String title;
	
	private String password;
	
	private String Img;
	
	private String userName;
	
	private List<SongInfo> songInfoList;

	private Timestamp createTime;	
	
}
