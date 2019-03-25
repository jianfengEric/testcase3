package com.tng.portal.ana.util;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Function;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaPermission;
import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.entity.AnaRoleFunctionPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoleFunctionPermissionUtil {

    private static final Logger logger = LoggerFactory.getLogger(RoleFunctionPermissionUtil.class);

    /**
     *  Check that the user is assigned functionCode Is there any specified requirement? permission
     *
     * @param account
     * @param functionCode
     * @param permissions
     * @return
     */
    public static boolean hasPermission(Account account, String functionCode, List<AnaPermission> permissions) {
        if (account == null) {
            logger.error("account == null");
            return false;
        }
        if (StringUtil.isEmpty(functionCode)) {
            logger.error("StringUtil.isEmpty(functionCode)");
            return false;
        }
        if (permissions == null || permissions.isEmpty()) {
            logger.error("permissions == null || permissions.isEmpty()");
            return false;
        }
        List<Role> roles = account.getRoles();
        if (roles == null) {
            logger.error("roles == null account.getAccountId():{}", account.getAccountId());
            return false;
        }
        List<AnaPermission> copyPermission = new ArrayList<>(permissions);
        for (Role role : roles) {
            List<Function> functions = role.getFunctions();
            for (Function function : functions) {
                if (functionCode.equals(function.getCode())) {
                    Iterator<AnaPermission> iterator = copyPermission.iterator();
                    while (iterator.hasNext()) {
                        AnaPermission permission = iterator.next();
                        int and = function.getPermissionSum() & permission.getId();
                        if (and == permission.getId()) {
                            iterator.remove();
                        }
                    }
                    if (copyPermission.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *  Check that the user is assigned functionCode Is there any specified requirement? permission
     *
     * @param account
     * @param functionCode
     * @param permissions
     * @return
     */
    public static boolean hasPermission(AnaAccount account, String functionCode, List<AnaPermission> permissions) {
        if (account == null) {
            logger.error("account == null");
            return false;
        }
        if (StringUtil.isEmpty(functionCode)) {
            logger.error("StringUtil.isEmpty(functionCode)");
            return false;
        }
        if (permissions == null || permissions.isEmpty()) {
            logger.error("permissions == null || permissions.isEmpty()");
            return false;
        }
        List<AnaRole> anaRoles = account.getAnaRoles();
        if (anaRoles == null) {
            logger.error("anaRoles == null account.getId():{}", account.getId());
            return false;
        }
        List<AnaPermission> copyPermission = new ArrayList<>(permissions);
        for (AnaRole anaRole : anaRoles) {
            List<AnaRoleFunctionPermission> anaRoleFunctions = anaRole.getAnaRoleFunctions();
            for (AnaRoleFunctionPermission anaRoleFunction : anaRoleFunctions) {
                if (functionCode.equals(anaRoleFunction.getAnaFunction().getCode())) {
                    Iterator<AnaPermission> iterator = copyPermission.iterator();
                    while (iterator.hasNext()) {
                        AnaPermission permission = iterator.next();
                        int and = anaRoleFunction.getPermissionsSum() & permission.getId();
                        if (and == permission.getId()) {
                            iterator.remove();
                        }
                    }
                    if (copyPermission.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
