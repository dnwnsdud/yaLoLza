package com.web.project.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Service;

import com.web.project.dto.Duo;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

import jakarta.transaction.Transactional;

@Service
@RepositoryDefinition(domainClass = SiteUser.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	public SiteUser findByUsernameIgnoreCase(String username);
	public SiteUser findByUsername(String username);
	public SiteUser findByNickname(String nickname);
	public SiteUser findByEmail(String email);
	
	public List<SiteUser> findAllByOrderByIdDesc();
	public Optional<SiteUser> findById(Long id);

	@Modifying
	@Transactional
	@Query("UPDATE SiteUser u SET u.password = :pw WHERE u.id = :indexId")
	void updatePw(@Param("pw") String pw, @Param("indexId") Long indexId);
	
}

