package com.sucl.springsecurity.security;

import com.sucl.springsecurity.security.filter.JwtAuthenticationTokenFilter;
import com.sucl.springsecurity.security.jwt.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@EnableWebSecurity
@Configuration
public class WebSecurityAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired(required = false)
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    private static String HTTP_TOKEN = "JWT";//Basic、JWT

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/*","/css/*","/login.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login.html").loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("-------------------success");
                        UserDetails userDetails = new User(authentication.getName(),"",Collections.EMPTY_LIST);
                        String token = jwtTokenHelper.generateToken(userDetails);
                        System.out.println("token:"+token);
                        PrintWriter writer = response.getWriter();
                        writer.write(token);
                        writer.flush();
                        writer.close();
                    }
                })
     //           .defaultSuccessUrl("/index.html")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        System.out.println("----------------failure");
                        PrintWriter writer = response.getWriter();
                        writer.write("login failure");
                        writer.flush();
                        writer.close();
                    }
                })
                .and()
                .logout().permitAll();
        http.httpBasic();
        http.csrf().disable();

        if(jwtAuthenticationTokenFilter!=null)
            http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);

    //    auth.inMemoryAuthentication().withUser("admin").password("123").roles("");

    }

}
