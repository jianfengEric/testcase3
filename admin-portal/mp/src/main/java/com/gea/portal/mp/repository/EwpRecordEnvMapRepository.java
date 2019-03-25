package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpRecordEnvMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpRecordEnvMapRepository extends JpaRepository<EwpRecordEnvMap, Long> {
}
