package com.guess.song.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
	@GetMapping({""})
	public String index(HttpServletRequest request) {
		String  userIp = request.getRemoteAddr();
		System.out.println(userIp);
		return "redirect:/board/main";
	}
	
	@GetMapping({"/test"})
	public String test() {		
		
		return "/test";
	}

}
