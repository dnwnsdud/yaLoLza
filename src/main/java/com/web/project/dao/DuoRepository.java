package com.web.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import com.web.project.dto.Duo;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;


@RepositoryDefinition(domainClass = Duo.class, idClass = Long.class)
public interface DuoRepository extends JpaRepository<Duo, Long>{
	List<Duo> findAllByOrderByIdDesc();
    List<Duo> findByMypositionOrderByIdDesc(Myposition myposition);
    List<Duo> findByYourpositionOrderByIdDesc(Yourposition yourposition);
    List<Duo> findByQueuetypeOrderByIdDesc(Queuetype queuetype);
    Optional<Duo> findById(Long id);
}