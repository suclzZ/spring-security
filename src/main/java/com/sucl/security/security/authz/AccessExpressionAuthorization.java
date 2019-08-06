package com.sucl.security.security.authz;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @1
 * 通过 access("@accessExpressionAuthorization.authz(request,authentication)")
 * @author sucl
 * @since 2019/7/9
 */
@Component
public class AccessExpressionAuthorization {

    public boolean authz(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal == null){
            return false;
        }
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/**");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if(authorities!=null && authorities.size()>0){
            return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_ACCESS");
        }
        return false;
    }

}
