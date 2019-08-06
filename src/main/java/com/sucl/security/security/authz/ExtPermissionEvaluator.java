package com.sucl.security.security.authz;

import com.sucl.security.security.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @4
 * @PermitAll("hasPermission('arg2','arg3')")
 *
 * @author sucl
 * @since 2019/7/9
 */
//@Component
public class ExtPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private UserPermissionService userPermissionService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        User user = (User) authentication.getPrincipal();
        List<String> permissions = userPermissionService.getPermissions(user.getUsername());
        return permissions.contains(permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        User user = (User) authentication.getPrincipal();
        List<String> permissions = userPermissionService.getPermissions(user.getUsername());
        return permissions.contains(permission);
    }
}
