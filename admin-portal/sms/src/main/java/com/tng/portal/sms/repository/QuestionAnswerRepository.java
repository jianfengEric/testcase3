package com.tng.portal.sms.repository;

import com.tng.portal.sms.entity.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer,Long> {

	@Query("select qa from QuestionAnswer qa where qa.questionAnswerPk.questionId = ?1") 
	List<QuestionAnswer> findByQuestionId(Long questionId);
	
	@Modifying
	@Query(value = "delete from question_answer where question_id=?1",nativeQuery = true)
	void deleteByQuestionId(Long questionId);
}
