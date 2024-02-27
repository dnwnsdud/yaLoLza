package com.web.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	UserService service;
	@Autowired
	OAuth2UserService oauth2service;
	@Bean
	PasswordEncoder bcrypt() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception{
		return http
				.csrf(csrf->
					csrf.ignoringRequestMatchers(
						"/champions/**",
						"/ranking/**",
						"/statistics/**",
						"/summoners/**"
					)
				)
				.authorizeHttpRequests(auth->
					auth
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers(
							"/yalolza.gg",
							"/yalolza.gg/**",
							"/yalolza.gg/champions/**",
							"/yalolza.gg/ranking/**",
							"/yalolza.gg/statistics/**",
							"/yalolza.gg/summoners/**", 
							"/login", 
							"/login/check", 
							"/oauth2/authorization/**",
							"/lol/**", 
							"/counter/**" ,
							"/img/**" ,
							"/css/**" ,
							"/js/**",
							"/json/**"
							
							
						).permitAll()
						.requestMatchers(
							"/dashboard"
						).hasAnyRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(login->
					login
						.loginPage("/login")
						.loginProcessingUrl("/login/check")
						.usernameParameter("username")
						.passwordParameter("password")
						.defaultSuccessUrl("/", true)
						.failureUrl("/login")
						.permitAll()
				)
				.oauth2Login(login->
					login
						.loginPage("/login")
						.userInfoEndpoint(end->
							end
								.userService(oauth2service)
						)
						.defaultSuccessUrl("/", true)
						.failureUrl("/login")
						.permitAll()
				)
				
				.logout(out->
					out
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.clearAuthentication(true)
						.invalidateHttpSession(true)
						.permitAll()
				)
				.rememberMe(me->
					me
						.key("rememberMe에 사용될 암호화 키")
						.tokenValiditySeconds(86400*7)
						.useSecureCookie(true)
						.rememberMeParameter("remember")
						.userDetailsService(service)
						.alwaysRemember(false)
				)
				.getOrBuild();
	}
}
