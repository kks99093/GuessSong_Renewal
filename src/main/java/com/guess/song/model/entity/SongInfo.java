package com.guess.song.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;


@Data
@Entity
public class SongInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer songPk;
	
	private String youtubeUrl;
	
	private String answer;
	
	private String hint;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private SongBoard songBoard;
	
	@Column
	@CreationTimestamp
	private Timestamp createTime;

}
