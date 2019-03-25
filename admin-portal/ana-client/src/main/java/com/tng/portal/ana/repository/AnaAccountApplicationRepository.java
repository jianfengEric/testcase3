package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaAccountApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2017/8/10.
 */
@Repository
public interface AnaAccountApplicationRepository extends JpaRepository<AnaAccountApplication,Long> {

	List<AnaAccountApplication> findByBindingAccountId(String accountId);

	List<AnaAccountApplication> findByAnaAccount(AnaAccount anaAccount);

	List<AnaAccountApplication> findByAnaApplication_CodeAndBindingAccountId(String applicationCode, String accountId);
	
	@Query("from AnaAccountApplication where anaApplication.code=?1 and bindingAccountId=?2")
	public AnaAccountApplication findByBinding(String applicationCode,String bindingAccountId);
	
}
