package com.guess.song.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.guess.song.handller.LoginFailUreHandller;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable();
    	
        http.authorizeHttpRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/auth/loginProc")
                        .defaultSuccessUrl("/")
                        .failureHandler(loginFailHandller()));
        
        
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/"));
        return http.build();
    }
    
	@Bean
	public AuthenticationFailureHandler loginFailHandller() {
		return new LoginFailUreHandller();
	}
	

}
