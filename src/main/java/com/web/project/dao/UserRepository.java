package com.web.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Service;

import com.web.project.dto.Duo;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

@Service
@RepositoryDefinition(domainClass = SiteUser.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	public SiteUser findByUsernameIgnoreCase(String username);
	public SiteUser findByUsername(String username);
	public SiteUser findByNickname (String nickname);
	
	public List<SiteUser> findAllByOrderByIdDesc();
	public Optional<SiteUser> findById(Long id);
	
}

