package com.guess.song.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.guess.song.model.entity.SongInfo;

@Repository
public interface SongInfoRepository extends JpaRepository<SongInfo, Integer> {
   
    
    int countByCategoryAndYearBetween(String category, int beforeYears, int afterYears);
    
    int countByYearBetween(int beforeYears, int afterYears);
    
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE category = :category AND year >= :beforeYears AND year <= :afterYears ORDER BY RAND() limit :count ")
	List<SongInfo> findSongList(String category, int beforeYears, int afterYears, int count);
	
	@Query(nativeQuery = true, value=" SELECT * FROM SongInfo WHERE year >= :beforeYears AND year <= :afterYears ORDER BY RAND() limit :count ")
	List<SongInfo> findSongList(int beforeYears, int afterYears, int count);
    
    SongInfo findBySongPk(int songPk);
    
    @Transactional
    int deleteBySongPk(int songPk);

}
