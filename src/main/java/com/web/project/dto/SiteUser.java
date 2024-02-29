package com.web.project.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.web.project.dto.enumerated.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SiteUsers")
@SequenceGenerator(
        name = "seq",
        sequenceName = "seq_SiteUsers",
        initialValue = 100000,
        allocationSize = 1
)
public class SiteUser implements UserDetails, OAuth2User {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	    private Long id;

	    @Column(unique = true, nullable = false)
	    private String username;

	    @Column(unique = true, nullable = false)
	    private String nickname;

	    private String password;

	    @Column(unique = true)
	    private String email;

	    String provider;

	    @Enumerated(EnumType.STRING) // UserRole enum의 문자열을 사용하도록 지정
	    private UserRole autho = UserRole.USER; // 기본값으로 USER 설정

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        ArrayList<GrantedAuthority> autho = new ArrayList<>();
	        // ROLE_ 권한을 넣어줘야 한다
	        autho.add(new SimpleGrantedAuthority("ROLE_" + autho));
	        return autho;
	    }

	    //인증 (Authentication)
	    //인가 (Authorization)

	    //	@ElementCollection
//		Set<String> auth = new HashSet<String>(); // 인가 중복 되면 안도
//		public void AddAuth(String auth) {
//			this.auth.add(auth);
//		}
//		public void RemoveAuth(String auth) {
//			this.auth.remove(auth);
//		}
	//
//		@Override
//		public Collection<? extends GrantedAuthority> getAuthorities() {
//			ArrayList<GrantedAuthority> autho = new ArrayList<>();
//			// ROLE_ 권한을 넣어줘야 한다
//			for(String sauth : auth) {
//				autho.add(new SimpleGrantedAuthority("ROLE_" + sauth));
//			}
//			return autho;
//		}
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
