package com.gea.portal.ewp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpCompanyForm;
import com.gea.portal.ewp.entity.EwpKeyPerson;

/**
 * Created by dong on 2018/8/27.
 */
@Repository
public interface EwpKeyPersonRepository extends JpaRepository<EwpKeyPerson, Long>{

	List<EwpKeyPerson> findByEwpCompanyForm(EwpCompanyForm ewpFormId);

}
