package com.tng.portal.ana.authority.spel;

import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.authority.AnaAuthority;
import com.tng.portal.ana.bean.Function;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Zero on 2016/11/15.
 */
public class AnaMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    private List<String> roles;
    private List<Function> functions;
    private String appCode;

    public AnaMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
        if(authentication instanceof AnaPrincipalAuthenticationToken){
            this.setVariables();
        }
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }

    public boolean hasAnyPermission(String function){
        List<String> funCodes = functions.stream().map(item -> item.getCode()).collect(Collectors.toList());
        return funCodes.contains(function);//sonarmodify
    }

    public boolean hasApplicaation(String appCode){
        return appCode.equals(this.appCode);//sonarmodify
    }

    public boolean hasAnyRole(){
        return roles != null && !roles.isEmpty();//sonarmodify
    }

    public boolean hasGivenRole(String roleName){
        return roles != null && !roles.isEmpty() && roles.contains(roleName);
    }

    public boolean hasGivenRoles(String roleInfo){
        if(roles!=null && !roles.isEmpty()){
            String [] roleArry = roleInfo.split(",");
            for(String role:roleArry){
                if(roles.contains(role)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission(String functionCode, Integer permission){
        for(Function anaFunction:functions){
            int resultPermission = anaFunction.getPermissionSum()&permission;
            String code = anaFunction.getCode();
            if(functionCode.equals(code) && (resultPermission==permission)){
                return true;
            }
        }
        return false;
    }

    public boolean hasPermission2(String functionCode, Integer permission){
        for(Function anaFunction:functions){
            int resultPermission = anaFunction.getPermissionSum()&permission;
            String code = anaFunction.getCode();
            if(functionCode.contains(code) && (resultPermission==permission)){
                return true;
            }
        }
        return false;
    }

    private void setVariables(){
        functions = new ArrayList<>();
        roles = new ArrayList<>();
        List<AnaAuthority> givenRoles = ((AnaPrincipalAuthenticationToken)authentication).getAuthorities();
        if(givenRoles != null && !givenRoles.isEmpty()){
            for(AnaAuthority authority : givenRoles){
                roles.add(authority.getAuthority());
                functions.addAll(authority.getFunctions());
                appCode = authority.getAppCode();
            }
        }
    }

}
