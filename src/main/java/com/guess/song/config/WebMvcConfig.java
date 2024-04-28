package com.guess.song.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	/*
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//WAR파일에서 배포시 view의 /upload/ 경로를 직접 지정
		registry.addResourceHandler("/upload/**")
		.addResourceLocations("file:///home/ubuntu/apps/upload/");
	}
	*/

}
