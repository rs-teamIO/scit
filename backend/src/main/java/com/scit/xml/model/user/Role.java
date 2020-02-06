package com.scit.xml.model.user;

import java.util.HashMap;
import java.util.Map;

public enum Role {

    AUTHOR("author"),
    EDITOR("editor");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName().toUpperCase();
    }

    private static final Map<String, Role> NAME_TO_ROLE = new HashMap<>();

    static {
        for (Role role : values()) {
            NAME_TO_ROLE.put(role.getName(), role);
        }
    }

    public static Role getRoleByName(String name) {
        return NAME_TO_ROLE.get(name);
    }
}
