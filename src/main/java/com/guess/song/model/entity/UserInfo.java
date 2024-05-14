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
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userPk;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String name;
	
	private String role;
	
	@Column
	@CreationTimestamp
	private Timestamp createTime;
}
