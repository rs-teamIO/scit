package com.scit.xml.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

public class JwtTokenDetailsUtil {

    private JwtTokenDetailsUtil() { }

    public static String getCurrentUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static String getCurrentUserUsername() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public static String getCurrentUserRole() {
        return new ArrayList<>(
                ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getAuthorities()).get(0).getAuthority();
    }
}
