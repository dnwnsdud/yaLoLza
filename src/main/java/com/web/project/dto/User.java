package com.web.project.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@SequenceGenerator(
	name = "seq",
	sequenceName = "seq_users",
	initialValue = 100000,
	allocationSize = 1
)
public class User implements UserDetails, OAuth2User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	Long id;
	
	String username;
	String password;
	
	@ElementCollection
	Set<String> auth;
	public void AddAuth(String auth) { this.auth.add(auth); }
	public void RemoveAuth(String auth) { this.auth.remove(auth); }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> autho = new ArrayList<>();
		for(String sauth : auth) {
			autho.add(new SimpleGrantedAuthority("ROLE_" + sauth));
		}
		return autho;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}
	@Override
	public String getName() {
		return username;
	}
}


















