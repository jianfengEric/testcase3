package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaRoleRepository extends JpaRepository<AnaRole,Long>{

    List<AnaRole> findByAnaAccounts(AnaAccount anaAccounts);

    AnaRole findById(Long id);
    List<AnaRole> findByName(String name);
    List<AnaRole> findByNameIn(List<String> nameList);

    List<AnaRole> findByIsActiveIsTrue(Sort sort);
    Page<AnaRole> findByIsActiveIsTrue(Pageable pageable);

    List<AnaRole> findByTypeAndIsActiveIsTrue(String type, Sort sort);

    Page<AnaRole> findByIsActiveIsTrueAndMid(String mid,Pageable pageable);

    List<AnaRole> findByMidAndIsActiveIsTrue(String mid,Sort sort);

    @Query("select a  from AnaRole a where upper(a.name)=upper(?1) and a.mid = ?2 and a.isActive=1")
    List<AnaRole> findByNameAndMid(String name,String mid);

    List<AnaRole> findByMid(String mid);

    List<AnaRole> findByNameAndMidNotNull(String name);

    @Query("select a  from AnaRole a where upper(a.name)=upper(?1) and a.mid is null and a.isActive=1")
    List<AnaRole> findByNameAndMidIsNull(String name);

    Page<AnaRole> findByIsActiveIsTrueAndMidIsNull(Pageable pageable);
    Page<AnaRole> findByIsActiveIsTrueAndTypeAndMidIsNull(String type,Pageable pageable);

    List<AnaRole> findByTypeAndMidIsNull(String type);

    List<AnaRole> findByTypeAndIsdefaultAndMidIsNull(String type, String isdefault);
    
	List<AnaRole> findByNameLikeAndNameNotLikeAndMid(String string, String string2, String mid);

	List<AnaRole> findByMidAndNameStartingWith(String mid, String name);

    List<AnaRole> findByIsActiveIsTrueAndMidIsNull(Sort sort);

    List<AnaRole> findByIsActiveIsTrueAndMid(String mid, Sort sort);


   /* @Query("select a  from AnaRole a where upper(a.name) like upper(?1) and a.isActive=1 and a.type = 'I'")
    List<AnaRole> findLikeName(String name);

    @Query("select a  from AnaRole a where upper(a.name) like upper(?1) and a.mid = ?2 and a.isActive=1 and a.type = 'E'")
    List<AnaRole> findLikeNameAndMid(String name,String mid);*/

    @Query("select a  from AnaRole a where a.roleType in (?1) and a.isActive=1 and a.type = 'I'")
    List<AnaRole> findByRoleType(List<String> roleTypes);

    @Query("select a  from AnaRole a where a.roleType in (?1) and a.mid = ?2 and a.isActive=1 and a.type = 'E'")
    List<AnaRole> findByRoleTypeAndMid(List<String> roleTypes, String mid);

    List<AnaRole> findByIsActiveIsTrueAndNameLike(Sort sort, String nameSearch);

    List<AnaRole> findByIsActiveIsTrueAndMidAndNameLike(String externalGroupId, Sort sort, String nameSearch);

    List<AnaRole> findByIsActiveIsTrueAndMidIsNullAndNameLike(Sort sort, String nameSearch);

    Page<AnaRole> findByIsActiveIsTrueAndNameLike(Pageable pageable, String nameSearch);

    Page<AnaRole> findByIsActiveIsTrueAndMidAndNameLike(String externalGroupId, Pageable pageable, String nameSearch);

    Page<AnaRole> findByIsActiveIsTrueAndTypeAndMidIsNullAndNameLike(String i, Pageable pageable, String nameSearch);

    Page<AnaRole> findByIsActiveIsTrueAndMidLike(String externalGroupIdSearch, Pageable pageable);
}
