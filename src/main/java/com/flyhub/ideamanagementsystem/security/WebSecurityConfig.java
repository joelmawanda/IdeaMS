package com.flyhub.ideamanagementsystem.security;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.flyhub.ideamanagementsystem.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/").authenticated()
//			.antMatchers("/list_users").authenticated()
//			.antMatchers("/list_ideas").authenticated()
//			.antMatchers("/list_notes").authenticated()
//			.antMatchers("/documents").authenticated()
//			.antMatchers("/notes").authenticated()
//			.antMatchers("/ideas").authenticated()
//			.anyRequest().permitAll()
//			.and()
//			.formLogin()
//				.usernameParameter("email")
//				.defaultSuccessUrl("/list_users")
//				.permitAll()
//			.and()
//			.logout().logoutSuccessUrl("/").permitAll();
//		
//	}
	


	
	@Override  
	protected void configure(HttpSecurity http) throws Exception { 
		http.authorizeRequests() 
		.antMatchers("/delete/**").hasAuthority("Admin") 
		.antMatchers("/edit/**").hasAnyAuthority("Admin", "User")
		.anyRequest().authenticated()  
		.and() 
		.formLogin().permitAll() 
		.and()
		.logout().permitAll()
		.and() 
		.exceptionHandling().accessDeniedPage("/403")
		; 
	}
	
}
