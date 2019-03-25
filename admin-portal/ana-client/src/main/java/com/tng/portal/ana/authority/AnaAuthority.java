package com.tng.portal.ana.authority;

import com.tng.portal.ana.bean.Function;
import com.tng.portal.ana.bean.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public class AnaAuthority implements GrantedAuthority{
    private Role role;

    private List<Function> functions;

    private String appCode;

    public AnaAuthority(Role role) {
        this.role = role;
        functions = role.getFunctions();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }

    public int getPermissions(){
        return role.getPermissionSum();
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
