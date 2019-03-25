package com.tng.portal.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tng.portal.sms.entity.QuestionTemplate;
import com.tng.portal.sms.entity.QuestionType;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionTemplate,Long>, JpaSpecificationExecutor<QuestionTemplate>, CrudRepository<QuestionTemplate,Long> {
	List<QuestionTemplate> findByIdIn(List<String> idList);
	
	QuestionTemplate findByQuestion(String question);

	List<QuestionTemplate> findByQuestionTypes(QuestionType type);

	QuestionTemplate findTopByQuestionAndIdNot(String question, Long id);
}
