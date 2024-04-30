package com.guess.song.model.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class GameRoom {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roomPk;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	private String salt;
	
	private String password;
	
	@Column(nullable = false)
	private int beforeYears;
	
	@Column(nullable = false)
	private int afterYears;
	
	@Column(nullable = false)
	private String category;
	
	@Column(nullable = false)
	private int count;
	
	@Column(nullable = false, length = 50)
	private String reader;
	
	@Column(nullable = false, length = 50)
	private int amount;
	
	@Column(nullable = false, length = 50)
	private int headCount;
	
	
	
	//없앨거
	@Column(nullable = false, length = 50)
	private int boardPk;
	
	@Column
	@CreationTimestamp
	private Timestamp createTime;


}
