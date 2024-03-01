package com.web.project.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.web.project.config.SecurityConfig;
import com.web.project.dao.UserRepository;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService extends DefaultOAuth2UserService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	    public SiteUser create(String username, String nickname, String email, String password) {
	        SiteUser user = new SiteUser();
	        user.setUsername(username);
	        user.setNickname(nickname);
	        user.setEmail(email);
	        user.setPassword(password);
	        this.userRepository.save(user);
	        return user;
	    }

	    public void deleteUser(Long id) {
	        this.userRepository.deleteById(id);
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        SiteUser user = userRepository.findByUsername(username);
	        if (user == null) throw new UsernameNotFoundException("Not Found");
	        return user;
	    }

	    @Override
		public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
			OAuth2User user = super.loadUser(userRequest);
			String provider = userRequest.getClientRegistration().getRegistrationId();
			
			System.out.println(provider);
			
			SiteUser dto = new SiteUser();
			
			if(provider.equalsIgnoreCase("naver")) {
				Map<String, Object> response = user.getAttribute("response");

				String id = response.get("id").toString().substring(0, 6);
				
				dto.setUsername(provider + "_" + id);
				dto.setNickname(provider + "/" + id);
				dto.setAutho(UserRole.USER);
				dto.setProvider(provider);
				SiteUser find = userRepository.findByUsername(dto.getUsername());
				if(find !=null) return find;			
//				for(String key : response.keySet()) {
//					System.out.printf("%s = %s\n", key, response.get(key));
//				}
				
				userRepository.save(dto);
			}
			else if(provider.equalsIgnoreCase("google")) {
				Map<String, Object> response = user.getAttributes();
				
				String id = response.get("sub").toString().substring(0, 6);
				
				dto.setUsername(provider + "_" + id);
				dto.setNickname(provider + "/" + id);
				SiteUser find = userRepository.findByUsername(dto.getUsername());
				if(find !=null) return find;		
					for(String key : response.keySet()) {
						System.out.printf("%s = %s\n", key, response.get(key));
					}
//				dto.AddAuth("USER");
				dto.setAutho(UserRole.USER);
				dto.setProvider(provider);
				userRepository.save(dto);			
			}
			else if(provider.equalsIgnoreCase("kakao")) {
				Map<String, Object> response = user.getAttributes();
				
				String id = response.get("id").toString().substring(0, 6);
				
				dto.setUsername(provider + "_" + id);
				dto.setNickname(provider + "/" + id);
				SiteUser find = userRepository.findByUsername(dto.getUsername());
				if(find !=null) return find;			
					for(String key : response.keySet()) {
						System.out.printf("%s = %s\n", key, response.get(key));
					}
//				dto.AddAuth("USER");
				dto.setAutho(UserRole.USER);
				dto.setProvider(provider);
				userRepository.save(dto);
			}
			else throw new OAuth2AuthenticationException("Not Found");
			
//			Map<String, Object> attrs = user.getAttributes();
//			for(String key : attrs.keySet()) {
//				System.out.printf("%s = %s\n", key, attrs.get(key).toString());
//			}
			return dto;
		}

		public List<SiteUser> getAllUsers() {
			return userRepository.findAllByOrderByIdDesc();
		}

		//닉네임 수정?
		public boolean changeNickname(String newNickname) {
			 String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		 
			 SiteUser currentUser = userRepository.findByUsername(currentUsername);
			 if (currentUser == null) {
				 throw new UsernameNotFoundException("현재 로그인한 사용자를 찾을 수 없습니다.");
			 }
			 SiteUser existingUser = userRepository.findByNickname(newNickname);
			 if (existingUser != null) {
				 return false;
			 }
			 currentUser.setNickname(newNickname);
			 userRepository.save(currentUser);
			 return true;
		 }


		 // 비밀번호 변경
		 public boolean changePass (Long userId, String oldPass, String newPass, String newPassConfirm) {
			// 사용자 정보 조회
			 SiteUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
			 if(!passwordEncoder.matches(oldPass, user.getPassword())) {
				 return false;
			 }
			 if(!newPass.equals(newPassConfirm)) {
				 return false; // 새비밀번호 불일치
			 }
			 // 새 비밀번호 저장
			 user.setPassword(passwordEncoder.encode(newPassConfirm));
			 userRepository.save(user);
			 return true; // 비밀번호 변경 성공
		 }
		

	}