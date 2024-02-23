package com.web.project.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.web.project.dao.UserRepository;
import com.web.project.dto.User;

@Service
public class UserService 
	extends DefaultOAuth2UserService
	implements UserDetailsService {
	
	@Autowired
	UserRepository userDao;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);
		String provider = userRequest.getClientRegistration().getRegistrationId();
		if(provider.equalsIgnoreCase("naver")) {
			// naver
		}
		else if(provider.equalsIgnoreCase("google")) {
			// google
		}		
		else if(provider.equalsIgnoreCase("kakao")) {
			// Kakao
		}
		else throw new OAuth2AuthenticationException("Not Found");
		// return dto;
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// user 검색 기능
//		if(user == null) throw new UsernameNotFoundException("Not Found");
//		return user;
		return null;
	}

}
