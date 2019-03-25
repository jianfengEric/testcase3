package com.tng.portal.sms.repository;

import com.tng.portal.sms.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionType,Long>, JpaSpecificationExecutor<QuestionType> {
	
	@Query("select qt from QuestionType qt") 
	Page<QuestionType> findQuestionTypesByPage(Pageable pageable);
	
	QuestionType findByNameAndIdNot(String name, Long id);

	QuestionType findByName(String name);

	QuestionType findByNameAndMid(String name, String mid);

	QuestionType findByNameAndMidAndIdNot(String name, String mid, Long id);
}
