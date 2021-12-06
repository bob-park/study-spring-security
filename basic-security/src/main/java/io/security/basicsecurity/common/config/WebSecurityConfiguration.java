package io.security.basicsecurity.common.config;

import lombok.extern.slf4j.Slf4j;
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
 *
 *
 * <p>
 * Form Login
 *
 * <pre>
 *      - 인증 받지 않는 사용자인 경우 Form Login 페이지로 이동한다.
 *      - 기본적으로 Spring Security 가 제공해주고 있으며, Custom 할 수 있다.
 *      - Session 및 인증 Token 생성 / 저장한다.
 *      - 세션에 저장된 인증 토큰으로 접근 / 인증 유지
 * </pre>
 */
@Slf4j
@EnableWebSecurity // ! 반드시 선언해주어야 웹 보안이 설정된다.
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * 인증
         */
        http
            .authorizeRequests()
            .anyRequest().authenticated();  // 어떠한 요청에서도 인증을 요구한다.

        /*
         * Form Login
         */
        http
            .formLogin() // 인증을 요구할 때, Form Login 으로 한다.
//            .loginPage("/loginPage") // 사용자 정의 Login Page - 이 경로는 접근할 때 인증에서 제외한다.
            .defaultSuccessUrl("/") // login 성공 후 이동 페이지
            .failureUrl("/loginPage?error=true") // 로그인 실패 후 이동 페이지
            .usernameParameter("userId") // 아이디 파라미터명 설정
            .passwordParameter("passwd") // password parameter 설정
            .loginProcessingUrl("/login_proc") // 로그인 form action url
//            .successHandler((request, response, authentication) -> {
//                log.info("authentication={}", authentication.getName());
//
//                response.sendRedirect("/");
//            }) // 로그인 성공 후 핸들러
//            .failureHandler((request, response, e) -> {
//                log.info("exception={}", e.getMessage());
//
//                response.sendRedirect("/loginPage");
//            }) // 로그인 실패 후 핸들러
            .permitAll() // 이 login page 는 인증을 받지 않도록 설정한다.
        ;
    }
}
