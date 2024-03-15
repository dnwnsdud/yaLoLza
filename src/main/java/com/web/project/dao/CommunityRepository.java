package com.web.project.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.project.dto.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer>{
	  Page<Community> findAllByCategoryIn(Collection<String> categories, String keyword, Pageable pageable);
@Query("select "
        + "distinct c "
        + "from Community c "
        + "where "
        + " ("
        + "     c.title like %:keyword% "
        + "     or c.content like %:keyword% "
        + "   )"
        
        + " AND LOWER(c.category) <> 'qna'")
Page<Community> findAllNotQnA(@Param("keyword") String keyword ,Pageable pageable);

    @Query("select "
        + "distinct c "
        + "from Community c "
        + "where "
        + "     (c.category = :category) "
        + "     and ("
        + "     c.title like %:keyword% "
        + "     or c.content like %:keyword% "
        + "   )")
    Page<Community> findAllByCategory(Pageable pageable, @Param("category") String category, @Param("keyword") String keyword);


    Page<Community> findAllByOrderByWriteviewDesc(Pageable pageable);

    @Query(
            "SELECT c FROM Community c WHERE c.category <> 'QnA' order by  c.writeview desc "
    )
    Page<Community> findallbest(Pageable pageable);


    List<Community> findTop10ByOrderByCreateDateDesc();
    
	List<Community> findAllByOrderByIdDesc();

}
