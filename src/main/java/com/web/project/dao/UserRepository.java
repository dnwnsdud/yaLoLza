package com.web.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Service;

import com.web.project.dto.SiteUser;

@Service
@RepositoryDefinition(domainClass = SiteUser.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<SiteUser, Long>{
	 public SiteUser findByUsernameIgnoreCase(String username);
	    public SiteUser findByUsername(String username);
}
