package io.security.basicsecurity.lecture_02.ch06.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Authentication Flow
 * <pre>
 *      -
 * </pre>
 */
//@EnableWebSecurity
//@Configuration
public class AuthenticationFlowConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated();

        http
            .formLogin();
    }
}
