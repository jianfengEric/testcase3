package com.gea.portal.apv.repository;

import com.gea.portal.apv.entity.ApprovalCategoryItem;
import com.tng.portal.common.enumeration.ApprovalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Dell on 2018/9/13.
 */
@Repository
public interface ApprovalCategoryItemRepository extends JpaRepository<ApprovalCategoryItem, Long>{
    ApprovalCategoryItem findByCode(ApprovalType code);
}
