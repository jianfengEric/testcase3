package com.gea.portal.ewp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpCompanyForm;
import com.gea.portal.ewp.entity.EwpOwner;

/**
 * Created by dong on 2018/8/27.
 */
@Repository
public interface EwpOwnerRepository extends JpaRepository<EwpOwner, Long>{

	List<EwpOwner> findByEwpCompanyForm(EwpCompanyForm ewpFormId);

}
