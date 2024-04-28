package com.guess.song.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guess.song.model.entity.SongBoard;
import com.guess.song.model.entity.SongInfo;

@Repository
public interface SongInfoRepository extends JpaRepository<SongInfo, Integer> {
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo ")
	List<SongInfo> findBySongList();
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE SongBoard_boardPk = ?")
	List<SongInfo> findByBoardPk(int boardPk);
	
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE SongBoard_boardPk = ? limit 1 ")
	SongInfo findBySongBoardPkLimit1(int boardPk);
	

}
