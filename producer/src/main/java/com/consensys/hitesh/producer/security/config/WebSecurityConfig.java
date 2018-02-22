package com.consensys.hitesh.producer.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.consensys.hitesh.producer.services.UserDetailsServiceImpl;

/**
 * @author hitjoshi
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
	     .antMatchers("/resources/**", "/registration","/view/pdf").permitAll()
	     .antMatchers("/**/*.css", "/**/*.png", "/**/*.js", "/**/*.html", "/fonts/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.and()
		.logout()
		.permitAll()
		.logoutSuccessUrl("/login");
		http.formLogin().defaultSuccessUrl("/welcome", true);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
		
		
	}
}



