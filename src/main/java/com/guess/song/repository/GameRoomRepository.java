package com.guess.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guess.song.model.entity.GameRoom;

@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Integer>{
	
	GameRoom findByRoomPk(int roomPk);
	


}
