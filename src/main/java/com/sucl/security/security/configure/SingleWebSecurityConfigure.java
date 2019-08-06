package com.sucl.security.security.configure;

import com.sucl.security.security.authz.ExtPermissionEvaluator;
import com.sucl.security.security.authz.UrlMatchAccessDecisionManager;
import com.sucl.security.security.authz.UrlMatchAccessDecisionVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sucl
 * @since 2019/7/9
 */
@ConditionalOnProperty(name = "security.single.enabled",havingValue = "true",matchIfMissing = true)
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@Configuration
public class SingleWebSecurityConfigure extends WebSecurityConfigurerAdapter {

    /**
     *  roles :最终将xxx转换成 new SimpleGrantedAuthority('ROLE_XXX')
     *  注意：
     *      1、如果用role，在验证是用hasRole不需要ROLE_前缀，用hasAuthority是需要加上ROLE_，
     *      而如果通过authorities来定义，则校验时用hasAuthority，除非有ROLE_前缀
     *      因此如果系统角色没不是按照ROLE_XX命名角色时就用authorities。最简单的处理，所有都用authorities
     *      2、roles('xxx').authorities('ROLE_XXX')或者authorities('ROLE_XXX').roles('xxx')后缀会覆盖前者
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        List<GrantedAuthority> auth1 = new ArrayList<>();
        auth1.add(new SimpleGrantedAuthority("ROLE_USER"));
        auth1.add(new UrlMatchAccessDecisionManager.UrlGrantedAuthority("/access/**"));
        List<GrantedAuthority> auth2 = new ArrayList<>();
        auth2.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        auth.inMemoryAuthentication()
                .withUser("user").password("123")
//                .roles("USER")
                .authorities(auth1)
                .and()
                .withUser("admin").password("123")
                .roles("ADMIN","ACCESS")
//                .authorities(auth2)
        ;
    }

    /**
     *  access : access("hasRole('ADMIN') or hasRole('USER')")//表达式
     *  hasRole : "xxx"
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();

        http.authorizeRequests()
//                .accessDecisionManager(accessDecisionManager())//定义自己的访问决策
                .antMatchers("/access/**").access("@accessExpressionAuthorization.authz(request,authentication)")
                .mvcMatchers("/admin/**")
                .hasRole("ADMIN")
//                .hasAuthority("ROLE_ADMIN")
                .mvcMatchers("/page/**")
                .hasRole("USER")
                .mvcMatchers("/**").authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                    // 覆盖SecurityMetadataSource
//                         object.setSecurityMetadataSource(object.getSecurityMetadataSource());
//					// 覆盖AccessDecisionManager
//                        object.setAccessDecisionManager(object.getAccessDecisionManager());

                        // 为默认的AffirmativeBased逻辑增加投票项，
                        AccessDecisionManager accessDecisionManager = object.getAccessDecisionManager();
                        if (accessDecisionManager instanceof AbstractAccessDecisionManager) {
                            ((AbstractAccessDecisionManager) accessDecisionManager).getDecisionVoters().add(new UrlMatchAccessDecisionVoter());
                        }
                        return object;
                    }
                })
        ;

        http.formLogin();

    }

    @Bean
    public UrlMatchAccessDecisionManager accessDecisionManager(){
        return new UrlMatchAccessDecisionManager();
    }

//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
//        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
//        handler.setPermissionEvaluator(new ExtPermissionEvaluator());
//        return handler;
//    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public static class PermissionSecurityConfigure extends GlobalMethodSecurityConfiguration {
        private ExtPermissionEvaluator extPermissionEvaluator;

        @Autowired
        public void setCustomPermissionEvaluator(ExtPermissionEvaluator extPermissionEvaluator) {
            this.extPermissionEvaluator = extPermissionEvaluator;
        }

        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(extPermissionEvaluator);
            return expressionHandler;
        }

        @Bean
        public ExtPermissionEvaluator permissionEvaluator(){
            return new ExtPermissionEvaluator();
        }
    }
}
