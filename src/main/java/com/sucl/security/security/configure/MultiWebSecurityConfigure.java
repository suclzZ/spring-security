package com.sucl.security.security.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置多个WebSecurityConfigurerAdapter一定要什么Order
 * 需要为不同的Configure配置不同的Filter，默认情况下是/**,应该配置不同的
 * security 过滤连模型
 * FilterChainProxy
 *  每一个Filter其实是一个新的FilterChainProxy，对应的是一个WebSecurityConfigurerAdapter
 * @author sucl
 * @since 2019/7/5
 */
@ConditionalOnMissingBean(SingleWebSecurityConfigure.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class MultiWebSecurityConfigure {

    @Order(0)
    @Configuration
    public class FormSecurityConfigure extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.httpBasic().disable();

            http.authorizeRequests()
                .mvcMatchers("/admin/**").authenticated()
                .mvcMatchers("/admin/student").hasAnyRole("MANAGER")
//                    .mvcMatchers("/admin/student").hasAnyAuthority("MANAGER")
                    ;

            http.antMatcher("/admin/**")//注意这里的写法,修改了filter的patter
                    .formLogin()
                    .loginPage("/admin/login").permitAll().defaultSuccessUrl("/admin/index")
            .and().logout().logoutUrl("/admin/logout").permitAll();

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("123").authorities("ROLE_MANAGER");
        }
    }

    @Order(1)
    @Configuration
    public class FormSecurityConfigure2 extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.httpBasic().disable();

            http.authorizeRequests()
                .mvcMatchers("/page/index").authenticated();
            http.antMatcher("/page/**")
                .formLogin().loginPage("/page/login").permitAll().defaultSuccessUrl("/page/index")
            .and().logout().logoutUrl("/page/logout").permitAll();

        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(staticUserDetailsService());
        }

        @Bean
        public UserDetailsService staticUserDetailsService(){
            return new UserDetailsService() {
                @Override
                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("USER"));
                    return new User("user","123",authorities);
                }
            };
        }
    }
}
