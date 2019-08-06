package com.sucl.security.security.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sucl
 * @since 2019/7/9
 */
@Service
public class UserPermissionService {
    public static Map<String,List<String>> permissionMap = new HashMap<>();

    static {
        permissionMap.put("admin", Arrays.asList("get","save","remove","update"));
        permissionMap.put("user", Arrays.asList("get","save"));
    }

    public List<String> getPermissions(String username){
        return permissionMap.get(username);
    }
}
