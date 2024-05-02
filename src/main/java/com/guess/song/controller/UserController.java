package com.guess.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.guess.song.model.entity.UserInfo;
import com.guess.song.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/joinProc")
	public String joinPorc (UserInfo userInfo, Model model) {
		int result = userService.join(userInfo);
		if(result == 1) {
			model.addAttribute("msg", " 회원가입이 완료 되었습니다. ");
			model.addAttribute("url", "/board/main");
		}else {
			model.addAttribute("msg", " 회원가입에 실패하였습니다. ");
			model.addAttribute("url", "/board/main");
		}

		return "/board/info";
	}

}
