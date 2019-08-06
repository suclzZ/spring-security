package com.sucl.security.security.authz;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**@3
 * 参考网上
 * 在WebSecurityConfigureAdapter对应的类中引入
 * @author sucl
 * @since 2019/7/9
 */
public class UrlMatchAccessDecisionVoter implements AccessDecisionVoter {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {

            if (authentication == null) {
                return ACCESS_DENIED;
            }

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            boolean hasPerm = false;

            for (GrantedAuthority authority : authorities) {
                if (!(authority instanceof UrlMatchAccessDecisionManager.UrlGrantedAuthority))
                    continue;
                UrlMatchAccessDecisionManager.UrlGrantedAuthority urlGrantedAuthority = (UrlMatchAccessDecisionManager.UrlGrantedAuthority) authority;
                if (StringUtils.isEmpty(urlGrantedAuthority.getAuthority()))
                    continue;
                //AntPathRequestMatcher进行匹配，url支持ant风格（如：/user/**）
                AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(urlGrantedAuthority.getAuthority());
                if (antPathRequestMatcher.matches(((FilterInvocation) object).getRequest())) {
                    hasPerm = true;
                    break;
                }
            }

            if (!hasPerm) {
                return ACCESS_DENIED;
            }

            return ACCESS_GRANTED;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
