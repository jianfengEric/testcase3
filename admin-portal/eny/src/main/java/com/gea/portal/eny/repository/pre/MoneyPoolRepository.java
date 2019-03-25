package com.gea.portal.eny.repository.pre;

import com.gea.portal.eny.entity.pre.MoneyPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dong on 2018/9/10.
 */
@Repository
public interface MoneyPoolRepository extends JpaRepository<MoneyPool, Integer> {

    MoneyPool findById(int id);
}
