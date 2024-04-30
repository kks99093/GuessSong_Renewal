package com.guess.song.model.param;

import java.util.List;

import com.guess.song.model.entity.SongInfo;

import lombok.Data;

@Data
public class SongInfoParam{
	
	private Integer boardPk;
	private List<String> youtubeUrl;
	private List<String> answer;
	private List<String> hint;
	private List<Integer> year;
	private List<String> category;
	
	private List<SongInfo> songInfoList;

}
