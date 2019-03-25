package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwpCompanyForm;
import com.gea.portal.ewp.entity.EwpDisputeContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/30.
 */
@Repository
public interface EwpDisputeContactRepository extends JpaRepository<EwpDisputeContact, Long> {

    List<EwpDisputeContact> findByEwpCompanyForm(EwpCompanyForm ewpFormId);

}
