package com.guess.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.guess.song.model.entity.UserInfo;
import com.guess.song.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/joinProc")
	public String joinPorc (UserInfo userInfo, RedirectAttributes reAt) {
		int result = userService.join(userInfo);
		reAt.addAttribute("result", result);
		return "redirect:/board/main?";
	}

}
