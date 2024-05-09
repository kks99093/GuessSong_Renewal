package com.guess.song.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.guess.song.auth.PrincipalDetailsService;
import com.guess.song.model.entity.GameRoom;
import com.guess.song.model.entity.SongInfo;
import com.guess.song.model.entity.UserInfo;
import com.guess.song.model.param.SongInfoParam;
import com.guess.song.service.BoardService;
import com.guess.song.util.StaticUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class BoardController {
	
	@Autowired
	private PrincipalDetailsService principalDetailService;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping({""})
	public String index(HttpServletRequest request) {
		
		return "redirect:/board/main";
	}
	
	
	@GetMapping("/board/main")
	public String main(HttpServletRequest request) {
		int count = StaticUtils.getCount() + 1;
		StaticUtils.setCount(count);
		String  userIp = request.getRemoteAddr();
		log.info("접속 : " + userIp + " ," + count + " 번째");
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
	
	@GetMapping("/admin/updSong")
	public String updSong(@PageableDefault(sort = {"answer"}, direction = Direction.DESC, size = 10) Pageable pageable, Model model) {
		Page<SongInfo> songInfoList = boardService.getSongInfoList(pageable);
		model.addAttribute("songInfoList", songInfoList);
		return "/board/updSong";
	}
	
	@PostMapping("/proc/insSong")	
	public String insSong(SongInfoParam songInfoParam, Model model) {		
		boardService.insSong(songInfoParam);
		model.addAttribute("msg", "노래 등록이 완료되었습니다.");
		model.addAttribute("url", "/board/main");
		return "/board/info";
	}
	

	@PostMapping("/board/gameBoard")
		
	public String gameBoard(UserInfo userInfo, Model model, GameRoom gameRoomParam) {

		GameRoom gameRoom = boardService.insGameRoom(gameRoomParam, userInfo);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("gameRoom", gameRoom);
		return "/board/gameBoard";
		
	}
	
	@GetMapping("/board/gameList")
	public String gameList(@PageableDefault(sort = {"createTime"}, direction = Direction.DESC, size = 10) Pageable pageable, Model model) {
		Page<GameRoom> gameRoomList =  boardService.selGameRoom(pageable);
		model.addAttribute("startIdx", (int)(gameRoomList.getPageable().getPageNumber()/10)*10);
		model.addAttribute("gameRoomList", gameRoomList);
	
		return "/board/gameList";
	}
	
	@PostMapping("/board/info")
	public String infoBoard(Model model) {
		
		return "/board/info";
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
