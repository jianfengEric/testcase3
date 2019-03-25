package com.gea.portal.dpy.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gea.portal.dpy.entity.DeployQueue;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;

/**
 * Created by Eric on 2018/9/12.
 */
public interface DeployQueueRepository extends JpaRepository<DeployQueue, Long>, JpaSpecificationExecutor<DeployQueue> {
	
	/**
	 * find By schedule_deploy_date
	 */
	@Query(value = "FROM DeployQueue where scheduleDeployDate<=?1 and status=?2 order by createDate asc ")
	public List<DeployQueue> findByScheduleDeployDate(Date scheduleDeployDate,DeployStatus status);
	
	@Query(" from DeployQueue where geaParticipantRefId=?1 and deployType=?2 and deployEnvir=?3 ")
	public List<DeployQueue> findByGeaParticipantRefId(String geaParticipantRefId,DeployType deployType,Instance deployEnvir);

	@Query(" from DeployQueue where createDate <=?1 and status=?2 and deployType = ?3 and geaParticipantRefId=?4 and Id!= ?5 and deployEnvir=?6")
	public List<DeployQueue> findByCreateDate(Date createDate, DeployStatus status, DeployType deployType, String geaParticipantRefId, Long id,Instance deployEnvir);

	List<DeployQueue> findByCreateDateLessThanAndStatusAndScheduleDeployDateIsNullOrderByCreateDateAsc(Date timestamp, DeployStatus status);

	@Query(" from DeployQueue where geaParticipantRefId=?1 and deployEnvir=?2 order by createDate desc")
	public List<DeployQueue> findByGeaParticipantRefIdAndDeployEnvir(String geaParticipantRefId, Instance deployEnvir);

	@Query(" from DeployQueue where status='PENDING_FOR_DEPLOY' and deployType=?1 and deployEnvir=?2 order by createDate desc ")
	public List<DeployQueue> findPending(DeployType deployType,Instance deployEnvir);
}
