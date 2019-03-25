package com.gea.portal.dpy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gea.portal.dpy.entity.DeploySyncMapResult;

/**
 * Created by Eric on 2018/9/12.
 */
public interface DeploySyncMapResultRepository extends JpaRepository<DeploySyncMapResult, Long> {
	
	/**
	 * find By deploy_queue_id
	 */
	@Query(value = "FROM DeploySyncMapResult where deployQueueId=?1 ")
	public List<DeploySyncMapResult> findByDeployQueueId(long deployQueueId);
	
	/**
	 * find By deploy_queue_id
	 */
	@Query(value = "FROM DeploySyncMapResult where deployQueueId=?1 and geaServerName=?2")
	public DeploySyncMapResult findByDeployQueueId(Long deployQueueId,String geaServerName);
}
