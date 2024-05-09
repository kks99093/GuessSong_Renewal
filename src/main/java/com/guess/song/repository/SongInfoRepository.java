package com.guess.song.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.guess.song.model.entity.SongInfo;

@Repository
public interface SongInfoRepository extends JpaRepository<SongInfo, Integer> {
	
	
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE SongBoard_boardPk = ?")
	List<SongInfo> findByBoardPk(int boardPk);
	
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE SongBoard_boardPk = ? limit 1 ")
	SongInfo findBySongBoardPkLimit1(int boardPk);
	
	@Query(nativeQuery = true, value=" SELECT COUNT(answer) FROM SongInfo WHERE category = ?1 AND year >= ?2 AND year <= ?3 ")
	int songInfoChk(String category, int beforeYears, int afterYears);
	
	@Query(nativeQuery = true, value=" SELECT COUNT(answer) FROM SongInfo WHERE year >= ?1 AND year <= ?2 ")
	int songInfoChk(int beforeYears, int afterYears);
	
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE category = ?1 AND year >= ?2 AND year <= ?3 ORDER BY RAND() limit ?4 ")
	List<SongInfo> findSongList(String category, int beforeYears, int afterYears, int count);
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE year >= ?1 AND year <= ?2 ORDER BY RAND() limit ?3 ")
	List<SongInfo> findSongList(int beforeYears, int afterTears, int count);
	
	SongInfo findBySongPk(int songPk);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = " DELETE FROM SongInfo WHERE songPk = ?1 ")
	int delSong(int songPk);
	

}
