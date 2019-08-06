package com.sucl.security.security.authz;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;

/**
 * @2
 * 注意对登录请求放行
 * 在WebSecurityConfigureAdapter对应的类中引入
 * @author sucl
 * @since 2019/7/9
 */
public class UrlMatchAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        boolean accessDenied = true;
        if(authentication!=null){

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if(authorities!=null){
                for(GrantedAuthority authority :authorities){
                    if(authority instanceof  UrlGrantedAuthority){
                        RequestMatcher requestMatcher = new AntPathRequestMatcher(authority.getAuthority());
                        if(requestMatcher.matches(((FilterInvocation) object).getRequest())){
                            accessDenied = false;
                            break;
                        }
                    }
                }
            }
        }
        if(accessDenied){
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }


    public static class UrlGrantedAuthority implements GrantedAuthority {

        private String uri;

        public UrlGrantedAuthority(String uri){
            this.uri = uri;
        }

        @Override
        public String getAuthority() {
            return uri;
        }
    }
}
