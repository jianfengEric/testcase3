package com.tng.portal.ana.util;

import com.tng.portal.ana.bean.Function;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.ana.entity.AnaFunction;
import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.entity.AnaRoleFunctionPermission;
import com.tng.portal.common.entity.AnaApplication;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class AnaBeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(AnaBeanUtils.class);

    public static List<Role> toRoles(List<AnaRole> anaRoleList){
        List<Role> roles = new ArrayList<>();
        anaRoleList.stream().forEach(anaRole -> {
            Role role = new Role();
            role.setName(anaRole.getName());
            Integer rolePermessionSum = 0;
            List<AnaRoleFunctionPermission> roleFunctionPermissionses = anaRole.getAnaRoleFunctions();
            List<Function> anaFunctions = new ArrayList<>();
            for (AnaRoleFunctionPermission item : roleFunctionPermissionses) {
                rolePermessionSum += item.getPermissionsSum();
                Function function = new Function();
                AnaFunction function1 = item.getAnaFunction();
                if (null != function1) {
                    function.setCode(item.getAnaFunction().getCode());
                }
                function.setPermissionSum(item.getPermissionsSum());
                anaFunctions.add(function);
            }
            AnaApplication anaApplication = anaRole.getAnaApplication();
            if (null != anaApplication) {
                role.setAppCode(anaApplication.getCode());
            }
            role.setFunctions(anaFunctions);
            role.setPermissionSum(rolePermessionSum);
            roles.add(role);
        });
        return roles;
    }

    public static void requestParamsCopyToBean(Object bean, HttpServletRequest request){
        ConvertUtils.register(new DateLocaleConverter(), Date.class);
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            String value = request.getParameter(name);
            try {
                BeanUtils.setProperty(bean, name, value);
            } catch (IllegalAccessException e1) {
                logger.error("IllegalAccess error",e);
            } catch (InvocationTargetException e1) {
                logger.error("InvocationTarget error",e);
            }
        }
    }

}
