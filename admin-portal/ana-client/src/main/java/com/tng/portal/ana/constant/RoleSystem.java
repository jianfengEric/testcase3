package com.tng.portal.ana.constant;

public enum RoleSystem {

    BD_Sales_Role("BD_Sales_Role"),
    MAM("MAM");


    private String name;

    RoleSystem(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
