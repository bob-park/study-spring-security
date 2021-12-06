package io.security.basicsecurity.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 사용자 정의 보안 기능 구현
 *
 * <pre>
 *      - WebSecurityConfigurerAdapter class
 *          - Spring Security 의 Web 보안 기능 초기화 및 설정
 *      - HttpSecurity class
 *          - 세부적인 보안 기능을 설정할 수 있는 API 제공
 * </pre>
 */
@EnableWebSecurity // ! 반드시 선언해주어야 웹 보안이 설정된다.
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated();  // start 어떠한 요청에서도 인증을 요구한다.

        http
            .formLogin(); // 인증을 요구할 때, Form Login 으로 한다.
    }
}
