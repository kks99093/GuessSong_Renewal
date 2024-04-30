package com.guess.song.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.guess.song.auth.PrincipalDetailsService;
import com.guess.song.model.dto.SongInfoDTO;
import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.SongBoard;
import com.guess.song.model.entity.UserInfo;
import com.guess.song.model.param.SongBoardParam;
import com.guess.song.model.param.SongInfoParam;
import com.guess.song.service.BoardService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class BoardController {
	
	@Autowired
	private PrincipalDetailsService principalDetailService;
	
	@Autowired
	private BoardService boardService;
	
	
	@GetMapping("/board/main")
	public String main(@PageableDefault(sort = {"createTime"}, direction = Direction.DESC, size = 24) Pageable pageable, Model model, @RequestParam(value="searchText", required=false) String searchText
			, @RequestParam(required = false) Integer result) {
		model.addAttribute("joinResult", result);
		Page<SongBoard> songBoardList = boardService.selSongBoardList(pageable, searchText);
		model.addAttribute("startIdx", (int)(songBoardList.getPageable().getPageNumber()/10)*10);
		model.addAttribute("songBoardList", songBoardList);
		if(searchText != null && !searchText.equals("")) {
			model.addAttribute("searchText", searchText);
		}
		return "/board/main";
	}
	
	
	@PostMapping("/board/main")
	public String postMain() {

		return "board/main";
	}
	

	@GetMapping("/admin/regSong")
	public String regSong(Model model) {		
		return "/board/regSong";
	}
	
	@PostMapping("/proc/insSong")	
	public String insSong(SongInfoParam songInfoParam) {
		
		boardService.insSong(songInfoParam);
		return "redirect:/board/main";
	}
	

	@PostMapping("/board/soloGameBoard")
	public String soloGameBoard(SongBoardParam songBoardParam, Model model) {
		List<SongInfoDTO> songList = boardService.findSongList(songBoardParam.getBoardPk());
		model.addAttribute("songList", songList);
		model.addAttribute("userName", songBoardParam.getUserName());
		return "/board/soloGameBoard";
	}
	

	@PostMapping("/board/gameBoard")
		
	public String gameBoard(UserInfo userInfo, Model model, GameRoom gameRoomParam) {
		
		
		GameRoom gameRoom = boardService.insGameRoom(gameRoomParam, userInfo);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("gameRoom", gameRoom);
		return "/board/multiGameBoard";
		
	}
	
	
	
	
	@GetMapping("/board/gameList")
	public String gameList(@PageableDefault(sort = {"createTime"}, direction = Direction.DESC, size = 10) Pageable pageable, Model model) {
		Page<GameRoom> gameRoomList =  boardService.selGameRoom(pageable);
		model.addAttribute("startIdx", (int)(gameRoomList.getPageable().getPageNumber()/10)*10);
		model.addAttribute("gameRoomList", gameRoomList);
	
		return "/board/gameList";
	}
	
	@GetMapping("/board/test")
	public String test() {

	
		return "/board/test";
	}
	
	

	
	// 세션 강제 부여	
//		@PostMapping("/board/main")
//		public String main(@AuthenticationPrincipal PrincipalDetails principalDetails,UserInfo userInfo, HttpSession session) {
//			UserDetails userDetail = principalDetailService.loadUserByUsername(userInfo.getUsername());
//			Authentication authentication = 
//			new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
//			SecurityContext securityContext = SecurityContextHolder.getContext();
//			securityContext.setAuthentication(authentication);
//			session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//			
//			return "redirect:/board/main";
//		}

}
