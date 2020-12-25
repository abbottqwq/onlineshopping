package com.edu.neu.project.config;

import com.edu.neu.project.authority.AuthorityEnum;
import com.edu.neu.project.dao.RememberMeDAO;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableWebSecurity
@Transactional
@Getter(AccessLevel.PRIVATE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsServiceImpl;
    private RememberMeDAO rememberMeDAO;

    @Autowired
    public void setRememberMeDAO(RememberMeDAO rememberMeDAO) {
        this.rememberMeDAO = rememberMeDAO;
    }

    @Autowired
    public void setUserDetailsServiceImpl(UserDetailsService userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers("/account/**")
                // .access("hasAnyAuthority(T(com.edu.neu.project.roles.AuthorityEnum).Manager, T(com.edu.neu.project.roles.AuthorityEnum).Customer)")
                .hasAnyAuthority(AuthorityEnum.Manager.getAuthority(), AuthorityEnum.Customer.getAuthority(), AuthorityEnum.Admin.getAuthority())
                .and()
                .authorizeRequests().antMatchers("/productmanager/**")
                .hasAnyAuthority(AuthorityEnum.Manager.getAuthority(), AuthorityEnum.Admin.getAuthority())
                .and()
                .authorizeRequests().antMatchers("/customer/**")
                .hasAuthority(AuthorityEnum.Customer.getAuthority())
                .and()
                .authorizeRequests().antMatchers("/admin/**")
                .hasAuthority(AuthorityEnum.Admin.getAuthority())
                .and()
                .exceptionHandling().accessDeniedPage("/403");


        httpSecurity.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/spring_security_check")
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/listproduct")
                .permitAll()
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
//                .and()
//                .rememberMe()
//                .rememberMeParameter("remember-me")
//                .key("rem-me-key")
//                .rememberMeCookieName("remember-me-cookie")
//                .tokenRepository(this.getRememberMeDAO()).userDetailsService(this.getUserDetailsServiceImpl())
//                //.tokenValiditySeconds(1 * 24 * 60 * 60)
//                .tokenValiditySeconds(180)
                .and().csrf().disable()
        ;
    }


}
