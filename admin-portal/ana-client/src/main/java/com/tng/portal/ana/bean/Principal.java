package com.tng.portal.ana.bean;

import javax.security.auth.Subject;
import java.io.Serializable;

/**
 * Created by Zero on 2016/11/10.
 */
public class Principal implements java.security.Principal,Serializable{
    private String name;
    private Object credentials;

    public Principal() {
    }

    public Principal(String name) {
        this.name = name;
    }

    public Principal(String name, Object credentials) {
        this.name = name;
        this.credentials = credentials;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean equals(Object object) {
        if(object == this){
            return true;
        }

        if(object == null){
            return false;
        }

        if(!object.getClass().equals(this.getClass())){
            return false;
        }
        Principal obj = (Principal)object;

        return obj.getName().equals(name);//sonarmodify
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 7;
        if(name != null){
            result = result * prime + name.hashCode();
        }
        return result;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
