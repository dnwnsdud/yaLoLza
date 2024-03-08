package com.web.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.web.project.api.controller.UserService;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
   @Autowired
   UserService service;
   
   

   @Autowired
   OAuth2UserService oauth2service;

   
//  @Bean
//  PasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//  }

	@Bean
	SecurityFilterChain chain(HttpSecurity http) throws Exception{
		return http
				.csrf(csrf->
					csrf.ignoringRequestMatchers(
							"**",
							"**/**",
						"/api/all",
						"/api/**",
						"/login/check",
						"/logout",
						"/oauth2/authorization/**",	
						"/champions/**",
						"/ranking/**",
						"/statistics/**",
						"/summoners/**",
						"/**",
						"/duo/**",
						"/img/**",
						"/css/**",
						"/js/**",
						"/monitoring/**"
					)
				)
				.authorizeHttpRequests(auth->
					auth
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers(
//							"/admin",
//							"/admin/**",
							"/static",
							"/yalolza.gg",
							"/yalolza.gg/static",
							"/yalolza.gg/static/**",
							"/yalolza.gg/**",
							"/yalolza.gg/champions",
							"/yalolza.gg/champions/**",
							"/yalolza.gg/ranking/**",
							"/yalolza.gg/statistics/**",
							"/yalolza.gg/summoners/**",
							"/yalolza.gg/more/**",
							"/duo.yalolza.gg/**", 
							"/duo.yalolza.gg/list/**", 
							"/duo.yalolza.gg/save/**", 
							"/duo.yalolza.gg/create/**", 
							"/duo.yalolza.gg/edit/**", 
							"/duo.yalolza.gg/view/**", 
							"/duo.yalolza.gg/delete/**", 
							"/talk.yalolza.gg/community/index",
							"/talk.yalolza.gg/community/list/**",
							"/talk.yalolza.gg/community/detail/**",
							"/talk.yalolza.gg/community/",
							"/home",
							"/index",
							"/yalolza.gg/user/login",
							"/yalolza.gg/user/signup",
							"/files/**",
							"/oauth2/authorization/**",
							"/lol/**", 
							"/counter/**" ,
							"/img/**" ,
							"/css/**" ,
							"/js/**",
							"/json/**",
							"/datas/**",
							"/info/**",
							"/monitoring/**"

						).permitAll()
						.requestMatchers(
								"/yalolza.gg/user/mypage"
								).hasAnyRole("USER", "ADMIN")
						.requestMatchers(
								"/admin/dashboard"
								).hasAnyRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(login->
					login
						.loginPage("/yalolza.gg/user/login")
						.usernameParameter("username")
						.passwordParameter("password")
						.defaultSuccessUrl("/yalolza.gg")
						.failureUrl("/yalolza.gg/user/login?error")
						.permitAll()
				)
				.oauth2Login(login->
					login
						.loginPage("/yalolza.gg/user/login")
						.userInfoEndpoint(end->
							end
								.userService(oauth2service)
						)
						.failureUrl("/yalolza.gg/user/login")
						.permitAll()
				)
				
				.logout(out->
					out
						.logoutRequestMatcher(new AntPathRequestMatcher("/yalolza.gg/user/logout"))
						.logoutSuccessUrl("/yalolza.gg")
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
	

    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setUseReferer(true); // 성공 후 referer를 사용하여 이전 페이지로 이동
        successHandler.setDefaultTargetUrl("/yalolza.gg"); // 기본적으로 이동할 페이지 설정
        return successHandler;
    }
    
}
