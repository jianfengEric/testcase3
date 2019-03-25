package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwpRecordEnvMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Owen on 2018/8/31.
 */
@Repository
public interface EwpRecordEnvMapRepository extends JpaRepository<EwpRecordEnvMap, Long>{
}
