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
                  "/duo/**"
               )
            )
            .authorizeHttpRequests(auth->
               auth
                  .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                  .requestMatchers(
//                        "**",
//                        "/**",
                     "/yalolza.gg",
                     "/yalolza.gg/**",
                     "/yalolza.gg/champions/**",
                     "/yalolza.gg/ranking/**",
                     "/yalolza.gg/statistics/**",
                     "/yalolza.gg/summoners/**", 
                     "/duo.yalolza.gg/duo/**", 
                     "/duo.yalolza.gg/duo/list/**", 
                     "/duo.yalolza.gg/duo/save/**", 
                     "/duo.yalolza.gg/duo/create/**", 
                     "/duo.yalolza.gg/duo/edit/**", 
                     "/duo.yalolza.gg/duo/view/**", 
                     "/duo.yalolza.gg/duo/delete/**", 
                     "/talk.yalolza.gg/community/index",
                     "/talk.yalolza.gg/community/list/**",
                     "/talk.yalolza.gg/community/detail/**",
                     "/talk.yalolza.gg/community/",
//                     "/talk.yalolza.gg/comment/**",
                     "/home",
                     "/index",
                     "/yalolza.gg/user/login",
                     "/yalolza.gg/user/signup",
                     "/files/**",
                     "/css/**",
                     "/img/**",
                     "/js/**",
                     "/oauth2/authorization/**"
                     
                  ).permitAll()
                  .requestMatchers(
                        "/user/mypage"
                        ).hasAnyRole("USER", "ADMIN")
                  .requestMatchers(
                        "/admin/dashboard"
                        ).hasAnyRole("ADMIN")
                  .anyRequest().authenticated()
            )
            .formLogin(login->
               login
                  .loginPage("/yalolza.gg/user/login")
//                  .loginPage("/members/login")
//                  .loginProcessingUrl("/login/check")
                  .usernameParameter("username")
//                  .usernameParameter("email")
                  .passwordParameter("password")
//                  .defaultSuccessUrl("/yalolza.gg", true)
                  .failureUrl("/yalolza.gg/user/login")
                  .permitAll()
            )
            .oauth2Login(login->
               login
//                  .loginPage("/members/login")
                  .loginPage("/yalolza.gg/user/login")
                  .userInfoEndpoint(end->
                     end
                        .userService(oauth2service)
                  )
                  .defaultSuccessUrl("/yalolza.gg", true)
//                  .failureUrl("/members/login/error")
                  .failureUrl("/yalolza.gg/user/login")
                  .permitAll()
            )
            
            .logout(out->
               out
                  .logoutRequestMatcher(new AntPathRequestMatcher("/yalolza.gg/user/logout"))
//                  .logoutUrl("/members/logout")
//                  .logoutSuccessUrl("/main")
//                  .logoutUrl("/user/logout")
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
   
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//        throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setUseReferer(true); // 성공 후 referer를 사용하여 이전 페이지로 이동
        successHandler.setDefaultTargetUrl("/yalolza.gg"); // 기본적으로 이동할 페이지 설정
        return successHandler;
    }
    
}
