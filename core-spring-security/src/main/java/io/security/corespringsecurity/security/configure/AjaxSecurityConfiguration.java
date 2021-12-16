package io.security.corespringsecurity.security.configure;

import io.security.corespringsecurity.security.filer.AjaxLoginProcessingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Order(0)
@EnableWebSecurity
@Configuration
public class AjaxSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**") // 해당 pattern 만 이 configuration 이 작동하도록 한다.
            .authorizeRequests()
            .anyRequest().authenticated();

        http
//            .addFilter() // 가장 마지막에 추가
//            .addFilterAfter() // target class 다음에 위치
//            .addFilterAt() // target class 에 대치
            .addFilterBefore(ajaxLoginProcessingFilter(),
                UsernamePasswordAuthenticationFilter.class); // target class 이전에 위치

        http.csrf().disable();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();

        // ! 반드시 AuthenticationManager 를 set 해주어야 한다.
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());

        return ajaxLoginProcessingFilter;
    }
}
