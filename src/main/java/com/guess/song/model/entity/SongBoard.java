package com.guess.song.model.entity;
//없애야함
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class SongBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer boardPk;
	
	@Column(nullable = false, length = 50)
	private String title;
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String salt;
	
	private String img;
	
	@OneToMany(mappedBy = "songBoard")
	private List<SongInfo> songInfoList;

	@Column
	@CreationTimestamp
	private Timestamp createTime;	
	

}
