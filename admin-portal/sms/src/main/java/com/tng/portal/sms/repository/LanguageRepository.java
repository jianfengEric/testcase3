package com.tng.portal.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tng.portal.sms.entity.SMSLanguage;

@Repository
public interface LanguageRepository extends JpaRepository<SMSLanguage,Long> {
	
	@Query("select lan from SMSLanguage lan where mid is null")
	Page<SMSLanguage> findLanguagesByPage(Pageable pageable);

	Page<SMSLanguage> findLanguagesByMid(String mid,Pageable pageable);
	
	SMSLanguage findByLanguage(String language);
	
	SMSLanguage findBySequence(int sequence);

	SMSLanguage findByLanguageAndMid(String language, String mid);

	SMSLanguage findBySequenceAndMid(int sequence, String mid);

	@Query("select lan from SMSLanguage lan where lan.mid is null and (lan.language like ?1 or lan.sequence like ?1)")
	Page<SMSLanguage> findLanguages(String search, Pageable pageable);

	@Query("select lan from SMSLanguage lan where lan.mid = ?1 and (lan.language like ?2 or lan.sequence like ?2)")
	Page<SMSLanguage> findLanguagesByMid(String mid, String search, Pageable pageable);

}
