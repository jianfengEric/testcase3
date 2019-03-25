package com.tng.portal.ana.constant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Dell on 2019/2/18.
 */
public enum RoleType {
//    ROOT,
    COMPLIANCE_TEAM_OPERATOR,
    COMPLIANCE_TEAM_MANAGER,
    FINANCE_TEAM_OPERATOR,
    FINANCE_TEAM_MANAGER,
    OPERATOR_TEAM_OPERATOR,
    OPERATOR_TEAM_MANAGER,
    GEA_ADMIN,
    EWALLET_PARTICIPANT_ADMIN,
    EWALLET_PARTICIPANT_MANAGER,
    EWALLET_PARTICIPANT_OPERATOR;

    public static List<String> findByUserType(String userType){
        return Stream.of(RoleType.values()).map(RoleType::name).filter(item -> item.endsWith("_" + userType.toUpperCase())).collect(Collectors.toList());
    }


    public static List<String> findByDepartmentAndUserType(String department, String userType){
        return Stream.of(RoleType.values()).map(RoleType::name).filter(item-> item.startsWith(department.toUpperCase() + "_") && item.endsWith("_" + userType.toUpperCase())).collect(Collectors.toList());
    }

}
